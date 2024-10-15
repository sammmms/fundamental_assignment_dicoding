package com.example.myfirstsubmission.data.retrofit

import com.google.gson.annotations.SerializedName

data class ListResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<Event>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)