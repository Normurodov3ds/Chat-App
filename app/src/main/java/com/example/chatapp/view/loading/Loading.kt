package com.example.chatapp.view.loading

import androidx.navigation.fragment.findNavController
import com.example.chatapp.App
import com.example.chatapp.R
import com.example.chatapp.databinding.LoadingScreenBinding
import com.example.chatapp.view.CommonFragment

class Loading : CommonFragment<LoadingScreenBinding>() {
    override fun getMyBinding(): LoadingScreenBinding {
        return LoadingScreenBinding.inflate(layoutInflater)
    }

    override fun setup(): Unit = with(binding) {
        val token = App.auth.currentUser?.uid
        if (token == null) {
            findNavController().navigate(R.id.action_loading_to_signUp)
        } else {
            if (token.toString() == App.shearedPref.getString(App.TOKEN_KEY, "")) {
                findNavController().setGraph(R.navigation.user_nav)

            } else {
                findNavController().navigate(R.id.action_loading_to_signIn)
            }
        }

    }
}