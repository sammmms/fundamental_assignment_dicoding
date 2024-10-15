package com.example.myfirstsubmission.data.repository

import android.app.Application
import com.example.myfirstsubmission.data.database.FavoriteEvent
import com.example.myfirstsubmission.data.database.FavoriteEventDao
import com.example.myfirstsubmission.data.database.FavoriteEventRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteEventRepository(application: Application) {
    private val favoriteEventDao: FavoriteEventDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteEventRoomDatabase.getDatabase(application)
        favoriteEventDao = db.favoriteDao()
    }

    suspend fun getAllFavoriteEvents(): List<FavoriteEvent> = favoriteEventDao.getAllFavoriteEvents()

    fun insertFavoriteEvent(favoriteEvent: FavoriteEvent) {
        executorService.execute { favoriteEventDao.insertFavoriteEvent(favoriteEvent) }
    }

    fun deleteFavoriteEvent(id: Int) {
        executorService.execute { favoriteEventDao.deleteFavoriteEvent(id) }
    }

    suspend fun getFavoriteEventById(id: Int): FavoriteEvent{
        return favoriteEventDao.getFavoriteEventById(id)
    }
}