package com.example.myfirstsubmission.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.myfirstsubmission.ui.favorite.FavoriteUpdateViewModel
import com.example.myfirstsubmission.ui.favorite.FavoriteViewModel

class ViewModelFactory private constructor(private val application: Application) : ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: ViewModelFactory(application)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(FavoriteUpdateViewModel::class.java) -> {
                    FavoriteUpdateViewModel(application) as T
                }
                isAssignableFrom(FavoriteViewModel::class.java) -> {
                    FavoriteViewModel(application) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}