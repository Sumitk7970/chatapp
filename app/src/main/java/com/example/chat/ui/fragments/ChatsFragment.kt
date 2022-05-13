package com.example.chat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.adapters.ChatAdapter
import com.example.chat.data.entity.Chat
import com.example.chat.data.remote.ChatDatabase
import com.example.chat.databinding.FragmentChatsBinding
import com.example.chat.ui.activities.MessageActivity
import com.example.chat.utils.FirebaseConstants
import com.example.chat.utils.FirebaseConstants.KEY_CHAT_ROOM_ID
import com.example.chat.utils.FirebaseConstants.KEY_CHAT_ROOM_NAME
import com.example.chat.utils.WrapContentLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatsFragment : Fragment(), ChatAdapter.ChatClickListener {
    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val chatDatabase = ChatDatabase()
        val query = chatDatabase.queryChats()
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Chat>()
            .setLifecycleOwner(this)
            .setQuery(query, Chat::class.java)
            .build()

        val adapter = ChatAdapter(requireContext(), recyclerViewOptions, this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onChatClick(chat: Chat) {
        val intent = Intent(context, MessageActivity::class.java).apply {
            putExtra(KEY_CHAT_ROOM_ID, chat.chatRoomId)
            putExtra(KEY_CHAT_ROOM_NAME, chat.chatRoomName)
            putExtra(FirebaseConstants.KEY_CHAT_ROOM_ICON_URL, chat.photoUrl)
        }
        context?.startActivity(intent)
    }
}