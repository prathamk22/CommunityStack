package com.example.communitystack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communitystack.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val userDb: CollectionReference = FirebaseFirestore.getInstance().collection(USER_DB)
    private val firebaseAuth = FirebaseAuth.getInstance()

    val login = MutableLiveData<State<User>>()

    fun login(user: User, pass: String) {
        viewModelScope.launch {
            login.value = State.loading()
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(user.email, pass).await()?.user?.uid
                PreferenceManager.instance.userUID = result?:""

                login.value = State.success(user)
            }catch (it: Exception){
                if (it is FirebaseAuthInvalidUserException){
                    signUp(user, pass)
                }else{
                    login.value = State.failed(it.message.toString())
                }
            }
        }
    }

    fun signUp(user: User, pass: String){
        viewModelScope.launch {
            login.value = State.loading()
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(user.email, pass).await()?.user?.uid
                val id = result?: randomId()
                userDb.document(id).set(user)

                PreferenceManager.instance.userUID = id

                login.value = State.success(user)
            }catch (it: Exception){
                login.value = State.failed(it.message.toString())
            }
        }
    }

}