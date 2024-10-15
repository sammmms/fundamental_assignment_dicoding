package com.example.myfirstsubmission.utils

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstsubmission.R
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.databinding.EventCardItemBinding
import com.example.myfirstsubmission.ui.DetailActivity
import com.example.myfirstsubmission.ui.favorite.FavoriteUpdateViewModel

class EventAdapter(val application: Application, val isFavoriteTab : Boolean = false): ListAdapter<Event, EventAdapter.MyViewHolder>(
    DIFF_CALLBACK
){
    private var events: List<Event> = listOf()
    private var viewModelUpdater: FavoriteUpdateViewModel = FavoriteUpdateViewModel(application)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EventCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(events[position])
        holder.itemView.setOnClickListener {
            val moveIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            var event = events[position]

            event = event.copy(isFavorite = viewModelUpdater.isFavorite(event.id))
            moveIntent.putExtra(DetailActivity.EXTRA_EVENT, event)

            holder.itemView.context.startActivity(moveIntent)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setEvents(events: List<Event>){
        val newEvents = mutableListOf<Event>()

        for(event in events){
            val newEvent = event.copy(isFavorite = viewModelUpdater.isFavorite(event.id))
            Log.e("EventAdapter", "isFavoriteTab: $isFavoriteTab, isFavorite: ${newEvent.isFavorite}")
            if(isFavoriteTab && !newEvent.isFavorite){
                continue
            } else{
                newEvents += newEvent
            }
        }

        val diffCallback = FavoriteEventDiffCallback(this.events, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.events = newEvents
        diffResult.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(private val binding: EventCardItemBinding, private val context: Context):RecyclerView.ViewHolder(binding.root){
        fun bind(event: Event){
            with(binding){
                cardTitle.text = event.name
                cardDescription.text = event.summary
                Glide.with(context).load(event.imageLogo).into(cardImageView)
                val isFavorite = viewModelUpdater.isFavorite(event.id)
                if(isFavorite){
                    favoriteIcon.setImageResource(R.drawable.baseline_bookmark_24)
                }
                else{
                    favoriteIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
                }
                favoriteButton.setOnClickListener {
                    viewModelUpdater.update(event)
                    val events = events.toMutableList()
                    if(isFavoriteTab){
                        events.remove(event)
                    } else {
                        val theEvent = events.find { it.id == event.id }
                        val index = events.indexOf(theEvent)
                        events[index] = event.copy(isFavorite = !isFavorite)
                        favoriteIcon.setImageResource(if(!isFavorite) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24)
                    }
                    setEvents(events)
                }
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>(){
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }

}