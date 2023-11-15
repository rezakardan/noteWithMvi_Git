package com.example.noteappwithmvii.data.repository

import com.example.noteappwithmvii.data.database.NoteDao
import com.example.noteappwithmvii.data.model.NoteEntity
import javax.inject.Inject

class NoteRepository@Inject constructor(private val dao: NoteDao) {


   suspend fun saveNote(note: NoteEntity)=dao.saveNote(note)


fun getOneNote(id:Int)=dao.getOneNoteById(id)


  suspend fun updateNote(note: NoteEntity)=dao.updateNote(note)



}