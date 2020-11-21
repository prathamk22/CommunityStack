package com.example.communitystack.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communitystack.util.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {

    private val postsDB: CollectionReference = FirebaseFirestore.getInstance().collection(POSTS_DB)
    private val userDB: CollectionReference = FirebaseFirestore.getInstance().collection(USER_DB)

    val allPosts = MutableLiveData<State<List<PostUserModel>?>>()

    fun getAllPosts() = viewModelScope.launch {
        allPosts.value = State.loading()
        try {
            val docs = postsDB.get().await()?.documents
            val users = userDB.get().await()?.documents

            val posts = docs?.map {postDoc->
                val post = postDoc.toObject(Posts::class.java)
                val postUser = users?.filter {user-> user.id == post?.user }?.firstOrNull()?.toObject(User::class.java)
                PostUserModel(posts = post, user = postUser)
            }

            allPosts.value = State.success(posts)
        }catch (e: Exception){
            allPosts.value = State.failed(e.localizedMessage?:"")
        }
    }

}