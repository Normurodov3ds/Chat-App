package com.example.chatapp.viewModel.settingsViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.App
import com.example.chatapp.model.UserModel

class SettingsViewModel : ViewModel() {

    private val _liveData = MutableLiveData<UiState>(UiState.Loading(true))
    val liveData: LiveData<UiState> = _liveData

    init {
        getUser()
    }

    private fun getUser() {
        App.realTimeDatabase.child("users").get().addOnSuccessListener {
            it.children.forEach {
                it.getValue(UserModel::class.java)?.let { model ->
                    if (model.token == App.shearedPref.getString(App.TOKEN_KEY, "")) {
                        _liveData.value = UiState.Success(model)
                    }
                }
            }
        }.addOnFailureListener {
            _liveData.value = UiState.Error(it.message.toString())
        }
    }
}

sealed class UiState {
    data class Success(val user: UserModel) : UiState()
    data class Error(val error: String) : UiState()
    data class Loading(var isLoading: Boolean) : UiState()

}