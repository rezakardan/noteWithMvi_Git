package com.example.noteappwithmvii.data.repository

import com.example.noteappwithmvii.data.database.NoteDao
import com.example.noteappwithmvii.data.model.NoteEntity
import javax.inject.Inject

class MainRepository@Inject constructor(private val dao: NoteDao) {

   fun allNotes()=dao.getAllNotes()

fun searchNotes(searching:String)=dao.searchNotes(searching)

fun filterByPriority(priority:String)=dao.filterByPriority(priority)

  suspend fun deleteNote(note: NoteEntity)=dao.deleteNote(note)

    fun getOneId(id:Int)=dao.getOneNoteById(id)

}