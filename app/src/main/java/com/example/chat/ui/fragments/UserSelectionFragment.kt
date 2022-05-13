package com.example.chat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.adapters.UserAdapter
import com.example.chat.data.entity.User
import com.example.chat.data.remote.UserDatabase
import com.example.chat.databinding.FragmentUserSelectionBinding
import com.example.chat.ui.viewmodels.CreateGroupViewModel
import com.example.chat.utils.WrapContentLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class UserSelectionFragment : Fragment(), UserAdapter.UserClickListener {
    private lateinit var binding: FragmentUserSelectionBinding
    private val userSelectionViewModel: CreateGroupViewModel by activityViewModels()
    private var users = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSelectionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        subscribeToObservers()
        binding.fabNext.setOnClickListener {
            next()
        }
    }

    private fun next() {
        findNavController().navigate(R.id.action_userSelectionFragment_to_createGroupFragment)
    }

    private fun setUpRecyclerView() {
        val userDatabase = UserDatabase()
        val query = userDatabase.usersCollection.orderBy("displayName", Query.Direction.ASCENDING)
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
        userSelectionViewModel.addOrRemoveUser(user)
    }

    private fun subscribeToObservers() {
        userSelectionViewModel.selectedUsers.observe(requireActivity()) {
            it?.let {
                users = it
            }
        }
    }
}