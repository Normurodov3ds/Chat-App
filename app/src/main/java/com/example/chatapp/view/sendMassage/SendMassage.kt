package com.example.chatapp.view.sendMassage

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.App
import com.example.chatapp.databinding.SendMassageScreenBinding
import com.example.chatapp.model.AdapterUsersModel
import com.example.chatapp.model.MassageModel
import com.example.chatapp.view.CommonFragment
import com.example.chatapp.viewModel.sendMassageViewModel.SendMassageAdapter
import com.example.chatapp.viewModel.sendMassageViewModel.SendMassageAdapter2
import com.example.chatapp.viewModel.sendMassageViewModel.SendMassageViewModel
import com.example.chatapp.viewModel.sendMassageViewModel.UiEventData
import java.text.SimpleDateFormat

class SendMassage : CommonFragment<SendMassageScreenBinding>() {
    override fun getMyBinding(): SendMassageScreenBinding {
        return SendMassageScreenBinding.inflate(layoutInflater)
    }

    private var userFreindModel: AdapterUsersModel? = null
    private val tokenMy by lazy {
        App.shearedPref.getString(App.TOKEN_KEY, null)
    }
    private val myNameIs by lazy {
        App.shearedPref.getString(App.NAME_KEY, null)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[SendMassageViewModel::class.java]
    }
    private val sendMassageAdapter by
    lazy {
        val token = App.shearedPref.getString(App.TOKEN_KEY, null)
        SendMassageAdapter(token!!)
    }

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    override fun setup() {
        binding.rvMassageList.adapter = sendMassageAdapter



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            userFreindModel =
                arguments?.getSerializable(App.TOKEN_KEY, AdapterUsersModel::class.java)
        } else {
            userFreindModel = arguments?.getSerializable(App.TOKEN_KEY) as AdapterUsersModel
        }

        if (tokenMy != null && userFreindModel?.token != null) {
            viewModel.getAllMassage(userFreindModel!!.token!!, tokenMy!!)
        }
        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiEventData.Error -> {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }

                is UiEventData.Loading -> {
                    // loading not impl
                }

                is UiEventData.Success -> {
                    sendMassageAdapter.submitList(it.list.toList())
                    val layoutManager = binding.rvMassageList.layoutManager as LinearLayoutManager
                    layoutManager.scrollToPosition(sendMassageAdapter.itemCount - 1)
                        App.shearedPref.edit().putInt(App.SEE_MASSAGE_COUNT, it.list.size).apply()
                }
            }
        }

        binding.sendBtn.setOnClickListener {
            val massage = binding.etMassageText.text.toString()
            if (massage.isNotEmpty()) {
                val time = SimpleDateFormat("HH:mm").format(System.currentTimeMillis())
                val massage = MassageModel(
                    name = myNameIs,
                    myToken = tokenMy,
                    friendToken = userFreindModel?.token,
                    massage = massage,
                    time = time

                )
                viewModel.sendMessage(massage)
            }
            binding.etMassageText.text.clear()
        }
    }
}