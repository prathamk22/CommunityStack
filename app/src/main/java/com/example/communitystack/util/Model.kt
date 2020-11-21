package com.example.communitystack.util

import com.google.firebase.Timestamp

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val message: String) : State<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}

data class User(
    var id: String = "",
    var name: String = "",
    var college: String = "",
    var school: String = "",
    var state: String = "",
    var email: String = ""
)

data class PostUserModel(
    val posts: Posts? = null,
    val user: User? = null
)

data class Posts(
    val user: String = "",
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val answerCount: Int = 0,
    val likes: Int = 0,
    val tags: List<String> = mutableListOf()
)