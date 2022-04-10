package com.example.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.databinding.ItemUsersBinding
import com.example.chat.models.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UserAdapter(options: FirestoreRecyclerOptions<User>,
                  private val userClickListener: UserClickListener):
    FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder>(options) {

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ItemUsersBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_users, parent, false
        ))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.binding.apply {
            nameText.text = model.displayName
            statusText.text = model.status
        }
        holder.itemView.setOnClickListener {
            userClickListener.onUserClick(model)
        }
    }

    interface UserClickListener {
        fun onUserClick(user: User)
    }
}