package com.example.proyekstory.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyekstory.databinding.FragmentLoginBinding
import com.example.proyekstory.ui.viewmodel.AuthViewModel
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.proyekstory.R
import com.example.proyekstory.ui.home.MainActivity
import com.example.proyekstory.ui.viewmodel.SettingViewModel
import com.example.proyekstory.ui.viewmodel.SettingViewModelFactory
import com.example.proyekstory.utils.SettingPreferences

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance((activity as AuthActivity).dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        authViewModel.loginUser.observe(viewLifecycleOwner) { // get user login response and save to datastore
            login ->
            settingViewModel.setUserPreferences(
                login.loginResult.token,
                login.loginResult.userId,
                login.loginResult.name,
                authViewModel.emailTemp.value ?: "None"
            )
        }

        settingViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token != "Not Set") {
                val mIntent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(mIntent)
            }
        }

        binding.buttonSignin.setOnClickListener{ //login
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            /*
            *  NOTES UNTUK REVIEWER :
            *  pengecekan logic tidak dilakukan di sini namun di file custom view
            *  pengecekan disini hanya jika input kosong tampilkan error field kosong
            * */

            if (TextUtils.isEmpty(email)) {
                binding.edLoginEmail.error = "Field must be filled"
            } else if (TextUtils.isEmpty(password)) {
                binding.edLoginPassword.error = "Field must be filled"
            } else if (binding.edLoginPassword.error?.length ?: 0 > 0) {
                binding.edLoginPassword.requestFocus()
            }
            else {
                authViewModel.login(email, password)
                authViewModel.loginUser.observe(viewLifecycleOwner) { loginResponse ->
                    if (!loginResponse.error) {
                        Toast.makeText(this.context, "Login succesful", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this.context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.registerText.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.container, RegisterFragment(), RegisterFragment::class.java.simpleName)
            }
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -50f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(binding.welcome, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.loginTitle, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.buttonSignin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(welcome, title, email, password, login, register)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        private const val TAG = "AuthViewModel"
    }
}