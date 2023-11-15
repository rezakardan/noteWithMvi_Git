package com.example.noteappwithmvii.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.util.NOTE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {


    @Insert
    suspend fun saveNote(note: NoteEntity)


    @Delete
    suspend fun deleteNote(note: NoteEntity)

@Update
    suspend fun updateNote(note: NoteEntity)

@Query("SELECT*FROM $NOTE_TABLE")
     fun getAllNotes():Flow<MutableList<NoteEntity>>


    @Query("SELECT*FROM $NOTE_TABLE WHERE id==:id")
     fun getOneNoteById(id:Int):Flow<NoteEntity>

    @Query("SELECT*FROM $NOTE_TABLE WHERE priority==:priority")
     fun filterByPriority(priority:String):Flow<MutableList<NoteEntity>>


     @Query("SELECT*FROM $NOTE_TABLE WHERE title LIKE '%'||:searching||'%'")
     fun searchNotes(searching:String):Flow<MutableList<NoteEntity>>

}