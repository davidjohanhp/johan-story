package com.example.proyekstory.utils

import com.example.proyekstory.model.Story

object DataDummy {
    fun generateDummyStories(): List<Story> {
        val stories = ArrayList<Story>()
        for (i in 0..10) {
            val story = Story(
                i.toString(),
                "created + $i",
                "name + $i",
                "description + $i",
                i.toString(),
                "id + $i",
                i.toString(),
            )
            stories.add(story)
        }
        return stories
    }
}