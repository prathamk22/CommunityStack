package com.example.communitystack

import android.app.Application

class CommunityApp : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        lateinit var instance: CommunityApp
    }

}