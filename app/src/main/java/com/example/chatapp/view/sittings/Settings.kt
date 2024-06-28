package com.example.chatapp.view.sittings

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.chatapp.App
import com.example.chatapp.R
import com.example.chatapp.databinding.SettingsBinding
import com.example.chatapp.model.UserModel
import com.example.chatapp.view.CommonFragment
import com.example.chatapp.viewModel.settingsViewModel.SettingsViewModel
import com.example.chatapp.viewModel.settingsViewModel.UiState

class Settings : CommonFragment<SettingsBinding>() {
    override fun getMyBinding(): SettingsBinding {
        return SettingsBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var user: UserModel

    override fun setup() = with(binding) {
        viewModel = ViewModelProvider(this@Settings)[SettingsViewModel::class.java]
        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Error -> {
                    progressBar.isVisible = false
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    // progress
                    progressBar.isVisible = true
                }

                is UiState.Success -> {
                    progressBar.isVisible = false
                    user = it.user
                     App.storage.reference.child("images/${user.name}").downloadUrl
                         .addOnSuccessListener {
                        userImage.load(it)
                    }
                    userName.setText(user.name)
                    lastName.setText(user.lastName)
                    etEmail.setText(user.email)
                }
            }
        }

        // logout

        logout.setOnClickListener {
            App.shearedPref.edit().clear().apply()
            findNavController().setGraph(R.navigation.login_nav)
        }


    }
}