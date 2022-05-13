package com.example.chat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.adapters.UserAdapter
import com.example.chat.data.entity.User
import com.example.chat.data.remote.UserDatabase
import com.example.chat.databinding.FragmentPeopleBinding
import com.example.chat.ui.activities.CreateGroupActivity
import com.example.chat.utils.WrapContentLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class UsersFragment : Fragment(), UserAdapter.UserClickListener {
    private lateinit var binding: FragmentPeopleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater)

        setUpRecyclerView()
        binding.clNewGroup.setOnClickListener {
            startActivity(Intent(context, CreateGroupActivity::class.java))
        }

        return binding.root
    }

    private fun setUpRecyclerView() {
        val userDatabase = UserDatabase()
        val query = userDatabase.queryUser()
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<User>()
            .setLifecycleOwner(this)
            .setQuery(query, User::class.java)
            .build()

        val adapter = UserAdapter(recyclerViewOptions, this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onUserClick(user: User) {
        // TODO: Show profile of user
    }
}