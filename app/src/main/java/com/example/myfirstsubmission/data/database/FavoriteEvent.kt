package com.example.myfirstsubmission.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEvent (
    @PrimaryKey
    @ColumnInfo(name="id")
    var id: Int = 0,

    @ColumnInfo(name="summary")
    var summary: String = "",

    @ColumnInfo(name="mediaCover")
    var mediaCover: String = "",

    @ColumnInfo(name="registrants")
    var registrants: Int = 0,

    @ColumnInfo(name="imageLogo")
    var imageLogo: String = "",

    @ColumnInfo(name="link")
    var link: String = "",

    @ColumnInfo(name="description")
    var description: String = "",

    @ColumnInfo(name="ownerName")
    var ownerName: String = "",

    @ColumnInfo(name="cityName")
    var cityName: String = "",

    @ColumnInfo(name="quota")
    var quota: Int = 0,

    @ColumnInfo(name="name")
    var name: String = "",

    @ColumnInfo(name="beginTime")
    var beginTime: String = "",

    @ColumnInfo(name="endTime")
    var endTime: String = "",

    @ColumnInfo(name="category")
    var category: String = "",
) : Parcelable