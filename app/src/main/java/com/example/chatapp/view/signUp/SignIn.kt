package com.example.chatapp.view.signUp

import com.example.chatapp.databinding.SignInBinding
import com.example.chatapp.view.CommonFragment

class SignIn : CommonFragment<SignInBinding>() {
    override fun getMyBinding(): SignInBinding {
        return SignInBinding.inflate(layoutInflater)
    }

    override fun setup() = with(binding) {

    }
}