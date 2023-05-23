package com.example.proyekstory.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyekstory.utils.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel (private val pref: SettingPreferences) : ViewModel() {
    fun getUserName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun getUserEmail(): LiveData<String> {
        return pref.getEmail().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getUserToken().asLiveData()
    }

    fun getUID(): LiveData<String> {
        return pref.getUID().asLiveData()
    }

    fun setUserPreferences(userToken: String, userUID: String, userName:String, userEmail: String) {
        viewModelScope.launch {
            pref.saveLoginSession(userToken,userUID,userName,userEmail)
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch {
            pref.deleteLoginSession()
        }
    }

}