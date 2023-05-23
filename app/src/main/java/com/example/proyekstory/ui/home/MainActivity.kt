package com.example.proyekstory.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekstory.R
import com.example.proyekstory.databinding.ActivityMainBinding
import com.example.proyekstory.model.Story
import com.example.proyekstory.ui.authentication.AuthActivity
import com.example.proyekstory.ui.maps.MapsActivity
import com.example.proyekstory.ui.upload.UploadActivity
import com.example.proyekstory.ui.viewmodel.*
import com.example.proyekstory.utils.SettingPreferences


class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        var token: String

        settingViewModel.getToken().observe(this) {
            if (it == "Not Set") {
                val intentDetail = Intent(this, AuthActivity::class.java)
                startActivity(intentDetail)
            }

            token = "Bearer $it"

            val mainViewModel by viewModels<MainViewModel>(){
//                ViewModelFactory(token)
                MainViewModelFactory(this, token)
            }

            val adapter = StoryAdapter()

            binding.rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            mainViewModel.story.observe(this, {
                adapter.submitData(lifecycle, it)
            })

//
//            mainViewModel.isLoading.observe(this) { loading ->
//                showLoading(loading)
//            }
//
//            mainViewModel.listStories.observe(this) { stories ->
//                setStoriesData(stories.listStory)
//            }
        }

        binding.fab.setOnClickListener {
            val intentDetail = Intent(this, UploadActivity::class.java)
            startActivity(intentDetail)
        }

        playAnimation()
    }

//    private fun setStoriesData(stories: List<Story>) {
//        val adapter = StoryAdapter(stories)
//        binding.rvStories.adapter = adapter
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                val pref = SettingPreferences.getInstance(dataStore)
                val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
                    SettingViewModel::class.java
                )
                settingViewModel.clearUserPreferences()

                val intentDetail = Intent(this, AuthActivity::class.java)
                startActivity(intentDetail)
                return true
            }
            R.id.maps -> {
                val intentDetail = Intent(this, MapsActivity::class.java)
                startActivity(intentDetail)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        var token: String

        settingViewModel.getToken().observe(this) {
            token = "Bearer $it"
            val mainViewModel by viewModels<MainViewModel>(){
//                ViewModelFactory(token)
                MainViewModelFactory(this, token)
            }

            val adapter = StoryAdapter()

            binding.rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
//            adapter.refresh()
            mainViewModel.refreshData()
            mainViewModel.story.observe(this, {
                adapter.submitData(lifecycle, it)
            })
//            mainViewModel.getStories(0, 20)
//            mainViewModel.listStories.observe(this) { stories ->
//                setStoriesData(stories.listStory)
//            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        val fab = ObjectAnimator.ofFloat(binding.fab, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(fab)
            start()
        }
    }
}