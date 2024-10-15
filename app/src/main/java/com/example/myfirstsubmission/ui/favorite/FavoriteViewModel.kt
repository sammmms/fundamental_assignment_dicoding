package com.example.myfirstsubmission.ui.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirstsubmission.data.repository.FavoriteEventRepository
import com.example.myfirstsubmission.data.retrofit.Event
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Suppress("DeferredResultUnused")
class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteEventRepository: FavoriteEventRepository = FavoriteEventRepository(application)

    private val _favoriteEvents : MutableLiveData<List<Event>> = MutableLiveData()
    val favoriteEvents = _favoriteEvents

    init {
        getFavoriteEvents()
    }

    fun getFavoriteEvents(){
        runBlocking {
            async{
                val events = favoriteEventRepository.getAllFavoriteEvents()
                val listEvents: List<Event> = events.flatMap {
                    listOf(
                        Event(
                            id = it.id,
                            summary = it.summary,
                            mediaCover = it.mediaCover,
                            registrants = it.registrants,
                            imageLogo = it.imageLogo,
                            link = it.link,
                            description = it.description,
                            ownerName = it.ownerName,
                            cityName = it.cityName,
                            quota = it.quota,
                            name = it.name,
                            beginTime = it.beginTime,
                            endTime = it.endTime,
                            category = it.category,
                            isFavorite = true
                        )
                    )
                }

                _favoriteEvents.postValue(listEvents)
            }
        }
    }
}