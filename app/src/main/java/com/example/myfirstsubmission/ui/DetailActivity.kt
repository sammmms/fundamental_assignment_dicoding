package com.example.myfirstsubmission.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myfirstsubmission.R
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.ui.favorite.FavoriteUpdateViewModel
import com.example.myfirstsubmission.ui.setting.SettingFragment
import com.example.myfirstsubmission.utils.ViewModelFactory

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var favoriteUpdateViewModel: FavoriteUpdateViewModel

    companion object{
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteUpdateViewModel = obtainViewModel()

        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById(R.id.detail_appbar)
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)

        var event: Event? = intent.getParcelableExtra(EXTRA_EVENT)

//      Set title to event name
        toolbar.title = event?.name

//      Navigation button on click (back)
        toolbar.setNavigationOnClickListener{
            finish()
        }

        setTextViewContent()

        val button : Button = findViewById(R.id.see_event_button)

//       Set button on click
        button.setOnClickListener {
            if(event == null){
                Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(event?.link))
            startActivity(browserIntent)
        }

        val favoriteButton : ImageView = findViewById(R.id.favorite_icon)
        favoriteButton.setOnClickListener {
            if(event == null){
                Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            event = event?.copy(isFavorite = !(event?.isFavorite ?: false))
            favoriteButton.setImageDrawable(AppCompatResources.getDrawable(this, if(event?.isFavorite == true) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24))

            Toast.makeText(this, if(event?.isFavorite == true) "Ditambahkan ke favorit" else "Dihapus dari favorit", Toast.LENGTH_SHORT).show()

            favoriteUpdateViewModel.update(event!!)
        }

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(SettingFragment.SETTING_PREFERENCES, Context.MODE_PRIVATE)
        val darkModeState: Boolean = sharedPreferences.getBoolean(SettingFragment.DARK_MODE_PREFERENCES, false)
        if(darkModeState){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
// Suppress the deprecation of get parcelable extra
    @Suppress("DEPRECATION")
    private fun setTextViewContent(){
        val title : TextView = findViewById(R.id.detail_title)
        val ownerName : TextView = findViewById(R.id.owner_name)
        val city: TextView = findViewById(R.id.city)
        val participants : TextView = findViewById(R.id.participants)
        val category: TextView = findViewById(R.id.category)
        val summary : TextView = findViewById(R.id.summary)
        val description : TextView = findViewById(R.id.detail_description)
        val dateRange: TextView = findViewById(R.id.date_range)
        val totalQuota: TextView = findViewById(R.id.total_quota)
        val totalRegistrant: TextView = findViewById(R.id.total_registrant)
        val eventImage : ImageView = findViewById(R.id.image_detail)
        val favoriteIcon: ImageView = findViewById(R.id.favorite_icon)

        val event : Event? = intent.getParcelableExtra(EXTRA_EVENT)

        val quota = event?.quota ?: 0
        val registrant = event?.registrants ?: 0


        title.text = event?.name
        ownerName.text = event?.ownerName
        city.text = event?.cityName
        participants.text = getString(R.string.available_quota, (quota-registrant).toString(), if(quota-registrant > 1) "s" else "" )
        category.text = event?.category
        summary.text = event?.summary
        description.text = HtmlCompat.fromHtml(event?.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
        dateRange.text = getString(R.string.placeholder_date_range, event?.beginTime, event?.endTime )
        totalQuota.text = getString(R.string.total_quota, quota.toString())
        totalRegistrant.text = getString(R.string.total_registrants, registrant.toString())
        if((event?.mediaCover?.isEmpty() ?: event?.imageLogo) != null)Glide.with(this@DetailActivity).load(event?.mediaCover ?: event?.imageLogo).into(eventImage)
        favoriteIcon.setImageDrawable(AppCompatResources.getDrawable(this, if(event?.isFavorite == true) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24))
    }

    private fun obtainViewModel(): FavoriteUpdateViewModel {
        val factory = ViewModelFactory.getInstance(application)
        return ViewModelProvider(this, factory)[FavoriteUpdateViewModel::class.java]
    }
}