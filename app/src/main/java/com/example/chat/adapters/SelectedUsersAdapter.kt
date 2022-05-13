package com.example.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.data.entity.User
import com.example.chat.databinding.ItemUsersBinding

class SelectedUsersAdapter(
    private val context: Context
) : RecyclerView.Adapter<SelectedUsersAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemUsersBinding.bind(itemView)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var selectedUsers: List<User>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_users, parent, false
            )
        )
    }

    override fun getItemCount() = selectedUsers.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = selectedUsers[position]
        holder.binding.apply {
            tvName.text = user.displayName
            tvStatus.text = user.status
            Glide.with(context).load(user.photoUrl).into(ivProfileImage)
        }
    }
}