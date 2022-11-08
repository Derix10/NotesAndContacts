package com.example.experiments.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val contact : String)