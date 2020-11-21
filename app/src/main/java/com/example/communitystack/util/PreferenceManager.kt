package com.example.communitystack.util

import android.content.Context
import com.example.communitystack.CommunityApp

class PreferenceManager private constructor(){

    private val SP_LOGGED_IN = "isLoggedIn"
    private val SP_USER_UID = "sp_userUID"
    companion object{
        val prefs = CommunityApp.instance.getSharedPreferences(COMMUNITY_SHARED_PREFS, Context.MODE_PRIVATE)
        val instance = PreferenceManager()
    }

    var userUID: String
        get() = prefs.getString(SP_USER_UID, "")?: ""
        set(value) {
            prefs.save(SP_USER_UID, value)
        }

    var isUserLoggedIn: Boolean
        get() = prefs.getBoolean(SP_LOGGED_IN, false)
        set(value) {
            prefs.save(SP_LOGGED_IN, value)
        }
}