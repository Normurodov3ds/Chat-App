package com.example.chatapp.viewModel.signUpViewModel

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.App
import com.example.chatapp.model.UserModel


class SignUpViewModel : ViewModel() {

    private val _liveDataAuth = MutableLiveData<UiEvent>()
    val liveDataAuth: LiveData<UiEvent> = _liveDataAuth

    private val _liveDataStore = MutableLiveData<UiEvent>()
    val liveDataStore: LiveData<UiEvent> = _liveDataStore

    fun addUserForAuth(email: String, password: String) {
        App.auth.createUserWithEmailAndPassword(email, password).addOnFailureListener {
            _liveDataAuth.value = UiEvent.Error(it.message.toString())
        }.addOnSuccessListener {
            _liveDataAuth.value = UiEvent.Success(it.user?.uid)
        }
    }

    fun addUserForDatabase(user: UserModel) {
        user.imageUri?.let {
            App.storage.reference.child("images/${user.name}")
                .putFile(it.toUri())
        }

        App.realTimeDatabase.child("users")
            .push().setValue(user)
            .addOnSuccessListener {
                _liveDataStore.value = UiEvent.Success(null)
            }.addOnFailureListener {
                _liveDataStore.value = UiEvent.Error(it.message.toString())
            }

    }

    fun addSharePref(token: String) {
        App.shearedPref.edit().putString(App.TOKEN_KEY, token).apply()
    }
    fun addMyName(name: String){
        App.shearedPref.edit().putString(App.NAME_KEY, name).apply()
    }

}

sealed class UiEvent {
    data class Success(val token:String?) : UiEvent()
    data class Error(val error: String) : UiEvent()
}