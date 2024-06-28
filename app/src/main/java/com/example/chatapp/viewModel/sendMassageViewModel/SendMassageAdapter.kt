package com.example.chatapp.viewModel.sendMassageViewModel

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.App

import com.example.chatapp.databinding.MassageItemBinding
import com.example.chatapp.model.MassageModel

class SendMassageAdapter(val token: String?) :
    ListAdapter<MassageModel, SendMassageAdapter.SendMassageViewHolder>(diffUtil) {


    inner class SendMassageViewHolder(private val viewBinding: MassageItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(massageModel: MassageModel) = with(viewBinding) {

            if (token != massageModel.myToken) {
                viewBinding.massageRoot.gravity = Gravity.END
            }else{
                viewBinding.massageRoot.gravity = Gravity.START
            }

            massageText.text = massageModel.massage
            time.text = massageModel.time
            App.storage.reference.child("images/${massageModel.name}").downloadUrl
                .addOnSuccessListener {
                    userImage.load(it)
                }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MassageModel>() {
            override fun areItemsTheSame(oldItem: MassageModel, newItem: MassageModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MassageModel, newItem: MassageModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMassageViewHolder {
        return SendMassageViewHolder(
            MassageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SendMassageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}