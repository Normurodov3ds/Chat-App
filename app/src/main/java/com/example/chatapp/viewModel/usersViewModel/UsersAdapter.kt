package com.example.chatapp.viewModel.usersViewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.App
import com.example.chatapp.databinding.ItemUserBinding
import com.example.chatapp.model.AdapterUsersModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersAdapter(private val onClick: (AdapterUsersModel?) -> Unit) :
    ListAdapter<AdapterUsersModel, UsersAdapter.UserAdapterHolder>(difUtil) {

    inner class UserAdapterHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: AdapterUsersModel) = with(binding) {
            userName.text = user.name
            userLastName.text = user.lastname
            val uri = App.storage.reference.child("images/${user.name}").downloadUrl
            uri.addOnSuccessListener {
                userImage.load(it)
            }
            val count = App.shearedPref.getInt(App.MASSAGE_COUNT_KEY, 0)
            if (count == 0) {
                binding.massageCountRoot.isVisible = false
            } else {
                binding.massageCountRoot.isVisible = true
                binding.massageCount.text = count.toString()
            }

            root.setOnClickListener {
                onClick(user)
            }
        }
    }

    companion object {
        val difUtil = object : DiffUtil.ItemCallback<AdapterUsersModel>() {
            override fun areItemsTheSame(
                oldItem: AdapterUsersModel,
                newItem: AdapterUsersModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AdapterUsersModel,
                newItem: AdapterUsersModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapterHolder {
        return UserAdapterHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserAdapterHolder, position: Int) {
        holder.bind(getItem(position))
    }
}