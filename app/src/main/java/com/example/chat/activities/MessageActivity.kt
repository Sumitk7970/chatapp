package com.example.chat.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.adapters.MessageAdapter
import com.example.chat.daos.MessageDao
import com.example.chat.databinding.ActivityMessageBinding
import com.example.chat.models.Message
import com.example.chat.utils.WrapContentLinearLayoutManager
import com.example.chat.utils.toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var messageDao: MessageDao
    private var senderId: String? = null
    private var receiverId: String? = null
    private var chatRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        messageDao = MessageDao()

        val name = intent.getStringExtra("name")
        receiverId = intent.getStringExtra("uid")

        supportActionBar?.title = name

        senderId = FirebaseAuth.getInstance().currentUser?.uid

        chatRoom = messageDao.generateChatRoomId(senderId!!, receiverId!!)

        setUpRecyclerView()
        binding.sendBtn.setOnClickListener {
            sendMessage()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendMessage() {
        val messageText = binding.messageField.text.toString()
        if(messageText.isEmpty()) {
            this.toast("Can't send empty message")
        } else {
            val message = Message(messageText, senderId!!, System.currentTimeMillis())
            val messageDao = MessageDao()
            messageDao.sendMessage(senderId!!, receiverId!!, chatRoom!!, message)
            binding.messageField.setText("")
        }
    }

    private fun setUpRecyclerView() {
        val messageDao = MessageDao()
        val query = messageDao.getMessages(chatRoom!!)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Message>()
            .setLifecycleOwner(this)
            .setQuery(query, Message::class.java)
            .build()
        adapter = MessageAdapter(recyclerViewOptions)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = WrapContentLinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, true)
    }

}