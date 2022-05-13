package com.example.chat.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chat.data.entity.User

class CreateGroupViewModel: ViewModel() {

    private val _selectedUsers = MutableLiveData<MutableList<User>>()
    val selectedUsers: LiveData<MutableList<User>> = _selectedUsers

    private var _selectedPhoto = MutableLiveData<Uri>()
    val selectedPhoto: LiveData<Uri> = _selectedPhoto

    init {
        _selectedUsers.postValue(mutableListOf())
    }

    fun addOrRemoveUser(user: User) {
        val users = _selectedUsers.value
        users?.let {
            if (users.contains(user)) {
                users.remove(user)
            } else {
                users.add(user)
            }
        }
    }

    fun addPhoto(uri: Uri) {
        _selectedPhoto.postValue(uri)
    }
}