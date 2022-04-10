package com.example.chat.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.daos.UserDao
import com.example.chat.databinding.ItemChatBinding
import com.example.chat.models.Chat
import com.example.chat.models.User
import com.example.chat.utils.convertTimeInMillisToDate
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class ChatsAdapter(options: FirestoreRecyclerOptions<Chat>,
                   private val chatClickListener: ChatClickListener):
    FirestoreRecyclerAdapter<Chat, ChatsAdapter.ChatViewHolder>(options) {

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = ItemChatBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_chat, parent, false
        ))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Chat) {
        val receiverId = if (model.members[0] == FirebaseAuth.getInstance().currentUser?.uid) {
            model.members[1]
        } else model.members[0]
//        holder.binding.nameText.text = UserDao().getUserById(receiverId)
        holder.binding.lastMessageText.text = model.lastMessage.message
        holder.binding.lastMessageTimeText.text =
            convertTimeInMillisToDate(model.lastMessage.sendTime)

//        holder.itemView.setOnClickListener {
//            chatClickListener.onChatClick()
//        }
    }

    interface ChatClickListener {
        fun onChatClick(user: User)
    }
}