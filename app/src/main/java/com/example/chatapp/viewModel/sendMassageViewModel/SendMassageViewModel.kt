package com.example.chatapp.viewModel.sendMassageViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.App
import com.example.chatapp.model.MassageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendMassageViewModel : ViewModel() {

    private val _liveData = MutableLiveData<UiEventData>()
    val liveData: LiveData<UiEventData> = _liveData
    private lateinit var key: String
    private var boolean: Boolean = true

    val massageList = mutableListOf<MassageModel?>()
    fun getAllMassage(friendToken: String, myToken: String) {

        val str = friendToken.plus(myToken)
        val chars = str.toCharArray()
        key = chars.sorted().joinToString("")



        App.realTimeDatabase
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        CoroutineScope(Dispatchers.Main).launch {
                            _liveData.value = UiEventData.Loading(true)
                        }
                        massageList.clear()
                        snapshot.child("massages").child(key).children.forEach {
                            massageList.add(it.getValue(MassageModel::class.java))
                            CoroutineScope(Dispatchers.Main).launch {
                                _liveData.value = UiEventData.Success(massageList)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    CoroutineScope(Dispatchers.Main).launch {
                        _liveData.value = UiEventData.Error(error.message)
                    }
                }
            })
    }

    fun sendMessage(massage: MassageModel) {
        val key = massage.friendToken.plus(massage.myToken)
        val chars = key.toCharArray()
        val keySorted = chars.sorted().joinToString("")
        App.realTimeDatabase.child("massages").child(keySorted).push().setValue(massage)
    }
}


sealed class UiEventData {
    data class Loading(val loading: Boolean) : UiEventData()
    data class Success(val list: List<MassageModel?>) : UiEventData()
    data class Error(val error: String) : UiEventData()
}