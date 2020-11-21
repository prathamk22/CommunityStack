package com.example.communitystack.ui.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communitystack.util.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.lang.Exception

class AddViewModel : ViewModel() {

    val tagsArray: MutableList<String>
    private val postsDB: CollectionReference = FirebaseFirestore.getInstance().collection(POSTS_DB)
    private val userDB: CollectionReference = FirebaseFirestore.getInstance().collection(USER_DB)

    init {
        tagsArray = mutableListOf()
    }

    val addPost = MutableLiveData<State<Posts>>()

    fun addPost(posts: Posts) = viewModelScope.launch {
        addPost.value = State.loading()

        try {
            val result = postsDB.add(posts).await()?.id
            val list =
                userDB.document(PreferenceManager.instance.userUID).get().await()?.get("posts") as MutableList<String>

            list.add(result?:"")
            userDB.document(PreferenceManager.instance.userUID).update("posts", list).await()

            addPost.value = State.success(posts)
        }catch (e: Exception){
            addPost.value = State.failed(e.localizedMessage?:"")
        }
    }


}