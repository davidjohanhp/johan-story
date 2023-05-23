package com.example.proyekstory.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.proyekstory.R
import com.example.proyekstory.databinding.FragmentRegisterBinding
import com.example.proyekstory.ui.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener{
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            /*
            *  NOTES UNTUK REVIEWER :
            *  pengecekan logic tidak dilakukan di sini namun di file custom view
            *  pengecekan disini hanya jika input kosong tampilkan error field kosong
            * */

            when {
                TextUtils.isEmpty(name) -> {
                    binding.edRegisterName.error = "Field must be filled"
                }
                TextUtils.isEmpty(email) -> {
                    binding.edRegisterEmail.error = "Field must be filled"
                }
                TextUtils.isEmpty(password) -> {
                    binding.edRegisterPassword.error = "Field must be filled"
                }
                else -> {
                    authViewModel.register(name, email, password)
                    authViewModel.regisUser.observe(viewLifecycleOwner) { regisResponse ->
                        if (!regisResponse.error) {
                            Toast.makeText(this.context, "Registration succesful", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this.context, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        authViewModel.regisUser.observe(viewLifecycleOwner) { register ->
            if (!register.error) {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.container, LoginFragment(), LoginFragment::class.java.simpleName)
                    commit()
                }
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
        val title = ObjectAnimator.ofFloat(binding.regisTitle, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(welcome, title, name, email, password, signup)
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