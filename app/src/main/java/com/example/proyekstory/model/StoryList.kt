package com.example.proyekstory.model

import com.google.gson.annotations.SerializedName

data class StoryList(

    @field:SerializedName("listStory")
	val listStory: List<Story>,

    @field:SerializedName("error")
	val error: Boolean,

    @field:SerializedName("message")
	val message: String
)
