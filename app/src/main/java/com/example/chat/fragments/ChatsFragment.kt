package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.activities.MessageActivity
import com.example.chat.adapters.UserAdapter
import com.example.chat.daos.MessageDao
import com.example.chat.daos.UserDao
import com.example.chat.databinding.FragmentChatsBinding
import com.example.chat.models.Chat
import com.example.chat.models.User
import com.example.chat.utils.WrapContentLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class ChatsFragment : Fragment(), UserAdapter.UserClickListener {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater)

        setUpRecyclerView()

        return binding.root
    }

    private fun setUpRecyclerView() {
//        val messageDao = MessageDao()
//        val query = messageDao.getChats(FirebaseAuth.getInstance().currentUser?.uid!!)
        val usersDao = UserDao()
        val query = usersDao.usersCollection.orderBy("displayName", Query.Direction.ASCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(query, User::class.java)
            .build()

        adapter = UserAdapter(recyclerViewOptions, this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onUserClick(user: User) {
        val intent = Intent(context, MessageActivity::class.java)
        intent.putExtra("name", user.displayName)
        intent.putExtra("uid", user.uid)
        context?.startActivity(intent)
    }
}