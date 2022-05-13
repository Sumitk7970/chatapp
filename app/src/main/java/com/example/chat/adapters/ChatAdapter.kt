package com.example.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.data.entity.Chat
import com.example.chat.databinding.ItemChatBinding
import com.example.chat.utils.toDate
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(
    private val context: Context,
    options: FirestoreRecyclerOptions<Chat>,
    private val userClickListener: ChatClickListener
) : FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options) {

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ItemChatBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_chat, parent, false
        ))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Chat) {
        holder.binding.apply {
            tvChatName.text = model.chatRoomName
            tvLastMessage.text = model.lastMessage.message
            tvLastMessageTime.text = model.lastMessage.sendTime.toDate()
            Glide.with(context).load(model.photoUrl).into(ivProfileImage)
        }
        holder.itemView.setOnClickListener {
            userClickListener.onChatClick(model)
        }
    }

    interface ChatClickListener {
        fun onChatClick(chat: Chat)
    }
}