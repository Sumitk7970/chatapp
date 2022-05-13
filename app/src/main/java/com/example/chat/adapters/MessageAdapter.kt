package com.example.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.data.entity.Message
import com.example.chat.data.entity.User
import com.example.chat.data.remote.UserDatabase
import com.example.chat.databinding.GroupMessageReceiveBinding
import com.example.chat.databinding.MessageSentBinding
import com.example.chat.utils.toDate
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val SENT_MESSAGE = 1
const val RECEIVE_MESSAGE = 2
class MessageAdapter(options: FirestoreRecyclerOptions<Message>):
    FirestoreRecyclerAdapter<Message, RecyclerView.ViewHolder>(options) {

    class SentMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = MessageSentBinding.bind(itemView)
    }

    class ReceiveMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = GroupMessageReceiveBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == SENT_MESSAGE) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.message_sent, parent, false
            )
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.group_message_receive, parent, false
            )
            ReceiveMessageViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().currentUser?.uid == getItem(position).senderId) {
            SENT_MESSAGE
        } else {
            RECEIVE_MESSAGE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Message) {
        if(holder.javaClass == SentMessageViewHolder::class.java) {
            val sentViewHolder = holder as SentMessageViewHolder
            sentViewHolder.binding.apply {
                message.text = model.message
            }
        } else {
            val receiveViewHolder = holder as ReceiveMessageViewHolder
            receiveViewHolder.binding.apply {
                tvMessage.text = model.message
                tvSendTime.text = model.sendTime.toDate()
                CoroutineScope(Dispatchers.IO).launch {
                    UserDatabase().getUserById(model.senderId).addOnSuccessListener {
                        it?.let {
                            val user = it.toObject(User::class.java)
                            tvSenderName.text = user?.displayName
                        }
                    }
                }
            }
        }
    }
}