package com.example.chatapp.viewModel.sendMassageViewModel

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.App

import com.example.chatapp.databinding.MassageItemBinding
import com.example.chatapp.model.MassageModel
import com.example.chatapp.viewModel.sendMassageViewModel.SendMassageAdapter2.SendMassageView2Holder

class SendMassageAdapter2(val token: String?) : RecyclerView.Adapter<SendMassageView2Holder>() {

    private val list = mutableListOf<MassageModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun loading(list: List<MassageModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addItemAndWrite(item: MassageModel) {
        this.list.add(getLastPosition(), item)
        notifyItemChanged(getLastPosition())
    }

    private fun getLastPosition(): Int {
        return list.size - 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMassageView2Holder {
        return SendMassageView2Holder(
            MassageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SendMassageView2Holder, position: Int) {
        holder.bind(list[position])
    }


    inner class SendMassageView2Holder(private val viewBinding: MassageItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(massageModel: MassageModel) = with(viewBinding) {

            if (token.equals(massageModel.myToken.toString())) {
                viewBinding.massageRoot.gravity = Gravity.END
            }

            massageText.text = massageModel.massage
            time.text = massageModel.time
            App.storage.reference.child("images/${massageModel.name}").downloadUrl
                .addOnSuccessListener {
                    userImage.load(it)
                }
        }
    }
}