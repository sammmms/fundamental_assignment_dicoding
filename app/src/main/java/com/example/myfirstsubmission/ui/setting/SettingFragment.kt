package com.example.myfirstsubmission.ui.setting

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.myfirstsubmission.databinding.FragmentSettingBinding
import com.example.myfirstsubmission.utils.NotificationWorker
import java.util.concurrent.TimeUnit


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!

    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private lateinit var workManager: WorkManager

    companion object{
        const val SETTING_PREFERENCES = "setting_preferences"
        const val DARK_MODE_PREFERENCES = "dark_mode_preferences"
        const val NOTIFICATION_PREFERENCES = "notification_preferences"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val settingPreferences = context?.getSharedPreferences(SETTING_PREFERENCES, Context.MODE_PRIVATE)

        workManager = WorkManager.getInstance(requireContext())

//      Find dark mode switch
        val darkModeSwitch: SwitchCompat = binding.darkModeSwitch

//      Set switch to current saved state
        darkModeSwitch.isChecked = settingPreferences?.getBoolean(DARK_MODE_PREFERENCES, false) ?: false

//      Declare editor
        val editor = settingPreferences?.edit()

        darkModeSwitch.setOnCheckedChangeListener{ _, isChecked ->
//          Change saved state and commit
            editor?.apply{
                putBoolean(DARK_MODE_PREFERENCES, isChecked)
                apply()
            }

//          Change theme
            if(isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

//      Find notification switch
        val notificationSwitch: SwitchCompat = binding.notificationSwitch

//      Set switch to current saved state
        notificationSwitch.isChecked = settingPreferences?.getBoolean(NOTIFICATION_PREFERENCES, false) ?: false

//      Declare editor
        val editorNotification = settingPreferences?.edit()

        notificationSwitch.setOnCheckedChangeListener{ _, isChecked ->
//            Check permission
            if(Build.VERSION.SDK_INT >= 33){
                val packageManager = ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                if(packageManager == -1){
                    Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show()
                    return@setOnCheckedChangeListener
                }

            }

//          Change saved state and commit
            editorNotification?.apply{
                putBoolean(NOTIFICATION_PREFERENCES, isChecked)
                apply()
            }

//          Set worker
            if(isChecked){
                startNotificationWorker()
            }
            else {
                stopNotificationWorker()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startNotificationWorker(){
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()

        periodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.HOURS)
            .setConstraints(constraint)
            .build()

        workManager.enqueue(periodicWorkRequest)
    }

    private fun stopNotificationWorker(){
        workManager.cancelAllWork()
    }


}