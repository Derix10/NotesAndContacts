package com.example.experiments.data

import androidx.room.*
import com.example.experiments.model.ContactModel

@Dao
interface ContactsNDAO {
    @Query("SELECT * FROM contactmodel")
    fun getAllContact(): List<ContactModel>

    @Insert
    fun addContact(model: ContactModel)

    @Update    fun upDateContact(model: ContactModel)

    @Delete
    fun deleteContact(model: ContactModel)
}