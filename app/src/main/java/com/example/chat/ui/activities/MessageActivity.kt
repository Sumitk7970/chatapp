package com.example.chat.ui.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.chat.R
import com.example.chat.adapters.MessageAdapter
import com.example.chat.data.entity.Message
import com.example.chat.data.remote.MessageDatabase
import com.example.chat.databinding.ActivityMessageBinding
import com.example.chat.utils.FirebaseConstants.KEY_CHAT_ROOM_ICON_URL
import com.example.chat.utils.FirebaseConstants.KEY_CHAT_ROOM_ID
import com.example.chat.utils.FirebaseConstants.KEY_CHAT_ROOM_NAME
import com.example.chat.utils.WrapContentLinearLayoutManager
import com.example.chat.utils.toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var messageDatabase: MessageDatabase
    private var senderId: String? = null
    private var chatRoomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitleAndIcon()
        setContentView(binding.root)
        messageDatabase = MessageDatabase()

        senderId = FirebaseAuth.getInstance().currentUser?.uid
        chatRoomId = intent.getStringExtra(KEY_CHAT_ROOM_ID)

        setUpRecyclerView()
        binding.sendBtn.setOnClickListener {
            sendMessage()
        }
    }

    private fun setTitleAndIcon() {
        supportActionBar?.title = ""
        val chatRoomName = intent.getStringExtra(KEY_CHAT_ROOM_NAME)
        binding.tvTitle.text = chatRoomName

        val photoUrl = intent.getStringExtra(KEY_CHAT_ROOM_ICON_URL)
        Glide.with(this).load(photoUrl)
            .placeholder(R.drawable.ic_person)
            .circleCrop()
            .into(binding.ivIcon)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.changeTheme -> {
                changeTheme()
                true
            }
            R.id.signOut -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun changeTheme() {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun sendMessage() {
        val messageText = binding.messageField.text.toString()
        if(messageText.isEmpty()) {
            this.toast("Can't send empty message")
        } else {
            val message = Message(messageText, senderId!!, System.currentTimeMillis())
            val messageDatabase = MessageDatabase()
            messageDatabase.sendMessage(chatRoomId!!, message)
            binding.messageField.setText("")
        }
    }

    private fun setUpRecyclerView() {
        val messageDao = MessageDatabase()
        val query = messageDao.queryMessages(chatRoomId!!)
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