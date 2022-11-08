package com.example.experiments.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.experiments.model.ContactModel
@Database(entities = [ContactModel::class], version = 1)
abstract class ContactDataBase : RoomDatabase() {
    abstract fun contactDao(): ContactsNDAO?

    companion object {

        @Volatile
        private var instancee: ContactDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instancee ?: synchronized(LOCK) {
            instancee ?: buildDB(context).also {
                instancee = it
            }
        }

        private fun buildDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ContactDataBase::class.java,
                "DBContact_NAME"
            ).allowMainThreadQueries().build()
    }
}