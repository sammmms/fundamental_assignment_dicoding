@file:Suppress("DeferredResultUnused")

package com.example.myfirstsubmission.ui.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myfirstsubmission.data.database.FavoriteEvent
import com.example.myfirstsubmission.data.repository.FavoriteEventRepository
import com.example.myfirstsubmission.data.retrofit.Event
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class FavoriteUpdateViewModel(application: Application) : ViewModel() {
    private val favoriteEventRepository: FavoriteEventRepository = FavoriteEventRepository(application)

    fun update (event: Event){
        val event = FavoriteEvent(
            id = event.id,
            summary = event.summary,
            mediaCover = event.mediaCover,
            registrants = event.registrants,
            imageLogo = event.imageLogo,
            link = event.link,
            description = event.description,
            ownerName = event.ownerName,
            cityName = event.cityName,
            quota = event.quota,
            name = event.name,
            beginTime = event.beginTime,
            endTime = event.endTime,
            category = event.category,
        )
        runBlocking {
            async {
                val favoriteEvent = favoriteEventRepository.getFavoriteEventById(event.id)
                Log.e("FavoriteUpdateViewModel", "update: $favoriteEvent")
                if (favoriteEvent != null){
                    favoriteEventRepository.deleteFavoriteEvent(event.id)
                } else {
                    favoriteEventRepository.insertFavoriteEvent(event)
                }
            }
        }
    }

    fun isFavorite(id: Int): Boolean {
        var isFavorite = false
        runBlocking {
            async {
                val favoriteEvent = favoriteEventRepository.getFavoriteEventById(id)
                isFavorite = favoriteEvent != null
            }
        }
        return isFavorite
    }
}