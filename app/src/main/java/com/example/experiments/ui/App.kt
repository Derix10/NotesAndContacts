package com.example.experiments.ui

import android.app.Application
import android.content.SharedPreferences
import com.example.experiments.data.ContactDataBase
import com.example.experiments.data.NoteDatabase
import com.example.experiments.ui.utils.Prefs

class App : Application() {
private lateinit var preferences: SharedPreferences
    companion object {
        lateinit var db: NoteDatabase
        lateinit var dbContact: ContactDataBase

        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        db = NoteDatabase.invoke(this)
        dbContact = ContactDataBase.invoke(this)
        preferences = this.applicationContext.getSharedPreferences("settings", MODE_PRIVATE)
        prefs = Prefs(preferences)

    }
}
