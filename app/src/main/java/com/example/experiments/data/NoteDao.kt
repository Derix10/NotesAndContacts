package com.example.experiments.data

import androidx.room.*
import com.example.experiments.model.NoteModel


@Dao
interface NoteDao {
    @Query("SELECT * FROM notemodel")
    fun getAllNote(): List<NoteModel>

    @Query("SELECT * FROM notemodel ORDER BY title ASC")
    fun getAllNoteAlphaphit(): List<NoteModel>

    @Query("SELECT * FROM notemodel ORDER BY createdTime ASC")
    fun getAllNoteData(): List<NoteModel>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun addNote(model: NoteModel)

    @Update
    fun upDateNote(model: NoteModel)


    @Delete
    fun deleteNote(model: NoteModel)
}