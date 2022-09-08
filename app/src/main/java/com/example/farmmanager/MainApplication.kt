package com.example.farmmanager
// 사용법: https://juahnpop.tistory.com/219


import android.app.Application
import android.content.Context



class MainApplication: Application() {


    init{
        instance = this
    }


    companion object{
        private var instance:MainApplication? = null


        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}