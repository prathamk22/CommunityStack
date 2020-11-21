package com.example.communitystack.util

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.communitystack.CommunityApp
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resumeWithException

suspend fun <T> Task<T>.await(): T? {
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                throw CancellationException(
                    "Task $this was cancelled normally."
                )
            } else {
                result
            }
        } else {
            throw e
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                if (isCanceled)
                    cont.cancel()
                else
                    cont.resume(result){
                        cont.resumeWithException(it)
                    }
            } else {
                cont.resumeWithException(e)
            }
        }
    }
}

fun randomId() = UUID.randomUUID().toString()

fun SharedPreferences.save(key: String, value: Any) {
    val edit = this.edit()
    with(edit) {
        when (value) {
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> putString(key, value.toString())
        }
    }
    edit.apply()
}

inline fun <reified T : ViewModel> createViewModel() =
    ViewModelProvider.AndroidViewModelFactory.getInstance(
        CommunityApp.instance
    ).create(T::class.java)

fun Job.catch(onCatch: (e: java.lang.Exception) -> Unit){
    try {
        start()
    }catch (e: Exception){
        onCatch(e)
    }
}