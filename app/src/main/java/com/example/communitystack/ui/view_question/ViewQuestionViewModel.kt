package com.example.communitystack.ui.view_question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communitystack.util.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ViewQuestionViewModel : ViewModel() {

    var postId: String = ""
    val postLiveData = MutableLiveData<State<PostUserModel>>()
    val ansLiveData = MutableLiveData<List<AnswerUserModel>>()

    val sendAnsLiveData = MutableLiveData<State<List<AnswerUserModel>>>()

    private val postsDB: CollectionReference = FirebaseFirestore.getInstance().collection(POSTS_DB)
    private val userDB: CollectionReference = FirebaseFirestore.getInstance().collection(USER_DB)

    fun getPost(postId: String) = viewModelScope.launch {
        postLiveData.value = State.loading()

        val post = postsDB.document(postId).get().await()?.toObject(Posts::class.java)
        post?.id = post?.id.toString()
        val users = userDB.document(post?.user!!).get().await()?.toObject(User::class.java)

        val ansList = post.answers?.map { ans ->
            val ansUser = userDB.document(ans.user).get().await()?.toObject(User::class.java)
            AnswerUserModel(
                answer = ans,
                user = ansUser
            )
        }
        ansLiveData.value = ansList
        postLiveData.value = State.Success(PostUserModel(posts = post, user = users))
    }.catch {
        postLiveData.value = State.failed(it.localizedMessage ?: "")
    }

    fun postAns(ans: String) = viewModelScope.launch {
        sendAnsLiveData.value = State.loading()
        val ans = Answer(
            id = randomId(),
            user = PreferenceManager.instance.userUID,
            answer = ans,
            votes = 0
        )

        val previousAnswers = postsDB.document(postId).get().await()?.toObject(Posts::class.java)
            ?.answers?.toList()?: emptyList()
        val newAns = mutableListOf<Answer>()
        newAns.addAll(previousAnswers)
        newAns.add(ans)

        postsDB.document(postId).update("answers", newAns).await()

        val ansList = newAns.map { ans ->
            val ansUser = userDB.document(ans.user).get().await()?.toObject(User::class.java)
            AnswerUserModel(
                answer = ans,
                user = ansUser
            )
        }

        ansLiveData.value = ansList
        sendAnsLiveData.value = State.success(ansList)
    }.catch {
        sendAnsLiveData.value = State.failed(it.localizedMessage?:"")
    }


}