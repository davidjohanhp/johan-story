package com.example.proyekstory.model

import com.google.gson.annotations.SerializedName

data class StoryDetail(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("story")
	val story: Story
)