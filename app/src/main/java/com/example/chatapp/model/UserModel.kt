package com.example.chatapp.model

data class UserModel(
    val name: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    var imageUri: String? = null,
    var token:String? = null
)
