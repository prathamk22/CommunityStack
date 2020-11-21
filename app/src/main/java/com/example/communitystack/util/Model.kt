package com.example.communitystack.util

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

data class AnswerUserModel(
    val answer: Answer? = null,
    val user: User? = null
)

data class Posts(
    var user: String = "",
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var answerCount: Int? = 0,
    var likes: Int? = 0,
    var tags: List<String> = mutableListOf(),
    var answers: List<Answer>? = null,
    var isDuplicate: Boolean? = false
)

data class Answer(
    val id: String = "",
    val user: String = "",
    val answer: String = "",
    val votes: Long = 0
)
