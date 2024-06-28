package com.example.chatapp.model

import java.io.Serializable


data class AdapterUsersModel(
    val name: String? = null,
    val lastname: String? = null,
    val image: String? = null,
    val token: String? = null
): Serializable