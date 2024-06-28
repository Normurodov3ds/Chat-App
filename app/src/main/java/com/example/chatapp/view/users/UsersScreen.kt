package com.example.chatapp.view.users

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.chatapp.App
import com.example.chatapp.R
import com.example.chatapp.databinding.UsersScreenBinding

import com.example.chatapp.view.CommonFragment
import com.example.chatapp.viewModel.usersViewModel.UiStateInUsers
import com.example.chatapp.viewModel.usersViewModel.UsersAdapter
import com.example.chatapp.viewModel.usersViewModel.UsersViewModel

class UsersScreen : CommonFragment<UsersScreenBinding>() {
    override fun getMyBinding(): UsersScreenBinding {
        return UsersScreenBinding.inflate(layoutInflater)
    }


    private val viewModel by lazy {
        ViewModelProvider(this)[UsersViewModel::class.java]
    }
    private val adapter by lazy {
        UsersAdapter {
            val bundle = Bundle()
            bundle.putSerializable(App.TOKEN_KEY, it)
            findNavController().navigate(R.id.action_usersScreen_to_sendMassage, bundle)
        }
    }

    override fun setup() {
        binding.rvUserList.adapter = adapter
        viewModel.getAllUsers()
        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiStateInUsers.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }

                is UiStateInUsers.Loading -> {
                    // loading not implemented
                }

                is UiStateInUsers.Success -> {
                    adapter.submitList(it.list.toList())
                }

                is UiStateInUsers.User -> {
                    with(binding){
                        userName.text = it.user.name
                        userLastName.text = it.user.lastName
                        val uri = App.storage.reference.child("images/${it.user.name}").downloadUrl
                        uri.addOnSuccessListener {i->
                            imageUser.load(i)
                        }
                    }
                }
            }
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_usersScreen_to_settings)
        }

    }
}