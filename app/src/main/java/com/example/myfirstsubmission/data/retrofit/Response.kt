@file:Suppress("unused")

package com.example.myfirstsubmission.data.retrofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("event")
	val event: Event
)

data class Event(

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("mediaCover")
	val mediaCover: String,

	@field:SerializedName("registrants")
	val registrants: Int,

	@field:SerializedName("imageLogo")
	val imageLogo: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ownerName")
	val ownerName: String,

	@field:SerializedName("cityName")
	val cityName: String,

	@field:SerializedName("quota")
	val quota: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("beginTime")
	val beginTime: String,

	@field:SerializedName("endTime")
	val endTime: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("isFavorite")
	val isFavorite: Boolean
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString() ?: "",
		parcel.readString()?: "",
		parcel.readInt(),
		parcel.readString()?: "",
		parcel.readString()?: "",
		parcel.readString()?: "",
		parcel.readString()?: "",
		parcel.readString()?: "",
		parcel.readInt(),
		parcel.readString()?: "",
		parcel.readInt(),
		parcel.readString()?: "",
		parcel.readString()?: "",
		parcel.readString()?: "",
		parcel.readByte() != 0.toByte()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(summary)
		parcel.writeString(mediaCover)
		parcel.writeInt(registrants)
		parcel.writeString(imageLogo)
		parcel.writeString(link)
		parcel.writeString(description)
		parcel.writeString(ownerName)
		parcel.writeString(cityName)
		parcel.writeInt(quota)
		parcel.writeString(name)
		parcel.writeInt(id)
		parcel.writeString(beginTime)
		parcel.writeString(endTime)
		parcel.writeString(category)
		parcel.writeByte(if (isFavorite) 1 else 0)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Event> {
		override fun createFromParcel(parcel: Parcel): Event {
			return Event(parcel)
		}

		override fun newArray(size: Int): Array<Event?> {
			return arrayOfNulls(size)
		}
	}
}
