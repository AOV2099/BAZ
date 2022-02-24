package com.aov2099.baz

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

class Network {
    companion object{
        @RequiresApi(Build.VERSION_CODES.M)
        fun conExists(act: FragmentActivity): Boolean {
            val connectivityManager = act.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetwork != null
        }
    }
}