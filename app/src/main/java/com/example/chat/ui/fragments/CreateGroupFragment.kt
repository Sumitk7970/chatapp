package com.example.chat.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.adapters.SelectedUsersAdapter
import com.example.chat.data.entity.User
import com.example.chat.data.remote.ChatDatabase
import com.example.chat.databinding.FragmentCreateGroupBinding
import com.example.chat.ui.viewmodels.CreateGroupViewModel

class CreateGroupFragment : Fragment() {
    private lateinit var selectedUsersAdapter: SelectedUsersAdapter
    private lateinit var binding: FragmentCreateGroupBinding
    private val viewModel: CreateGroupViewModel by activityViewModels()
    private lateinit var users: MutableList<User>
    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGroupBinding.inflate(inflater)
        selectedUsersAdapter = SelectedUsersAdapter(requireContext())
        subscribeToObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivGroupIcon.setOnClickListener {
            choosePhoto()
        }
        binding.fabCreateGroup.setOnClickListener {
            createGroup()
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvSelectedUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selectedUsersAdapter
        }
    }

    private fun subscribeToObservers() {
        viewModel.selectedUsers.observe(requireActivity()) {
            it?.let {
                users = it
                selectedUsersAdapter.selectedUsers = it
            }
        }
        viewModel.selectedPhoto.observe(requireActivity()) {
            it?.let {
                selectedPhotoUri = it
                binding.ivGroupIcon.setImageURI(it)
            }
        }
    }

    private fun createGroup() {
        val chatRoomName = binding.etGroupName.text.toString()
        if (chatRoomName.isBlank()) return

        val members = mutableListOf<String>()
        for (user in users) {
            members.add(user.uid)
        }
        val chatDatabase = ChatDatabase()
        chatDatabase.addNewChatRoom(requireContext(), chatRoomName, selectedPhotoUri, members)
        activity?.finish()
    }

    private val launchForPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null && result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val uri = intent?.data
                uri?.let { it -> viewModel.addPhoto(it) }
            }
        }

    private fun choosePhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launchForPhoto.launch(intent)
    }
}