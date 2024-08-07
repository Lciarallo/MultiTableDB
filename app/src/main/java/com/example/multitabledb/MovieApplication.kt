package com.example.multitabledb

import android.app.Application
import com.example.multitabledb.data.AppDatabase

class MovieApplication: Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

}