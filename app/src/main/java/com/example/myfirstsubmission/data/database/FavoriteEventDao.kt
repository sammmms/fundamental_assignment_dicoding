package com.example.myfirstsubmission.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favoriteevent")
    suspend fun getAllFavoriteEvents(): List<FavoriteEvent>

    @Query("SELECT * FROM favoriteevent WHERE id = :id")
    suspend fun getFavoriteEventById(id: Int): FavoriteEvent

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteEvent(favoriteEvent: FavoriteEvent)

    @Query("DELETE FROM favoriteevent WHERE id = :id")
    fun deleteFavoriteEvent(id: Int)
}