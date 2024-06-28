package com.example.chatapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage

class App : Application() {

    companion object {
        lateinit var shearedPref: SharedPreferences
        lateinit var realTimeDatabase: DatabaseReference
        lateinit var auth: FirebaseAuth
        lateinit var storage: FirebaseStorage
        const val SHARED_FILE_NAME = "my_shared_file"
        const val TOKEN_KEY = "token"
        const val NAME_KEY = "my_name_is"
        const val MASSAGE_COUNT_KEY = "massage_count"
        const val SEE_MASSAGE_COUNT = "see_massage_count"

    }

    override fun onCreate() {
        super.onCreate()
        shearedPref = getSharedPreferences(SHARED_FILE_NAME, Context.MODE_PRIVATE)
        realTimeDatabase = Firebase.database.getReference()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
    }
}