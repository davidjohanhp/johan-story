package com.example.proyekstory.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.proyekstory.databinding.ActivityDetailBinding
import com.example.proyekstory.model.Story
import com.example.proyekstory.ui.viewmodel.*
import com.example.proyekstory.utils.SettingPreferences


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        val idStoryData = intent.getStringExtra(idStory)
        var token: String

        settingViewModel.getToken().observe(this) {
            token = "Bearer $it"
            val detailViewModel by viewModels<DetailViewModel>(){
                ViewModelFactory(token)
            }

            detailViewModel.isLoading.observe(this) { loading ->
                showLoading(loading)
            }

            if (idStoryData != null) {
                detailViewModel.getStory(idStoryData)
            }
            detailViewModel.storyDetail.observe(this) { storyDetail ->
                setStoryData(storyDetail.story)
            }
        }
    }

    private fun setStoryData(detailStory: Story) {
        binding.storyUsername.text = detailStory.name
        Glide.with(binding.root)
            .load(detailStory.photoUrl)
            .into(binding.storyImage)
        binding.storyDesc.text = detailStory.description
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val idStory = "id"
    }
}