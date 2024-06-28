package com.example.chatapp.viewModel.usersViewModel

import android.annotation.SuppressLint
import androidx.browser.trusted.Token
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.App
import com.example.chatapp.model.AdapterUsersModel
import com.example.chatapp.model.MassageModel
import com.example.chatapp.model.UserModel
import com.example.chatapp.viewModel.sendMassageViewModel.UiEventData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class UsersViewModel : ViewModel() {

    private val _liveData = MutableLiveData<UiStateInUsers>()
    val liveData: LiveData<UiStateInUsers> = _liveData
    private val userList = mutableListOf<AdapterUsersModel>()


     fun getMassageCount(myToken: String,friendToken: String) {
        val count = App.shearedPref.getInt(App.SEE_MASSAGE_COUNT, 0)
        CoroutineScope(Dispatchers.IO).launch {

            val massageList = mutableListOf<MassageModel>()
            val str = friendToken.plus(myToken)
            val chars = str.toCharArray()
            val key = chars.sorted().joinToString("")

            App.realTimeDatabase
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        CoroutineScope(Dispatchers.IO).launch {
                            massageList.clear()
                            snapshot.child("massages").child(key).children.forEach {
                                it.getValue(MassageModel::class.java)
                                    ?.let { it1 -> massageList.add(it1) }
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (count<massageList.size){
                                        App.shearedPref.edit().putInt(App.MASSAGE_COUNT_KEY,massageList.size-count).apply()

                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // error
                    }
                })
        }
    }

    fun getAllUsers() {
        val token = App.shearedPref.getString(App.TOKEN_KEY, "")
        App.realTimeDatabase.child("users").get()
            .addOnSuccessListener { item ->
                userList.clear()
                item.children.forEach {
                    it.getValue(UserModel::class.java)?.let { model ->
                        if (model.token != token) {
                            userList.add(
                                AdapterUsersModel(
                                    name = model.name,
                                    lastname = model.lastName,
                                    image = null,
                                    token = model.token
                                )
                            )
                            _liveData.value = UiStateInUsers.Success(userList.toList())
                        } else {
                            _liveData.value = UiStateInUsers.User(model)
                        }
                    }
                }
            }.addOnFailureListener {
                _liveData.value = UiStateInUsers.Error(it.message.toString())
            }
    }

    @SuppressLint("SimpleDateFormat")
    fun isOnline(myToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val time = SimpleDateFormat("HH:mm").format(System.currentTimeMillis())
            App.realTimeDatabase.child("IsOnline").child(myToken).push().setValue(time)
        }

    }
}

sealed class UiStateInUsers {
    data class Loading(val isLoading: Boolean) : UiStateInUsers()
    data class Success(val list: List<AdapterUsersModel>) : UiStateInUsers()
    data class Error(val error: String) : UiStateInUsers()
    data class User(val user: UserModel) : UiStateInUsers()
}