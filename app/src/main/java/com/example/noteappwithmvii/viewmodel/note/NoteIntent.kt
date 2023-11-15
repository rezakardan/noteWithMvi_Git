package com.example.noteappwithmvii.viewmodel.note

import com.example.noteappwithmvii.data.model.NoteEntity

sealed class NoteIntent {

object SpinnersData:NoteIntent()

data class SaveNote(val note:NoteEntity):NoteIntent()



    data class DetailNote(val id:Int):NoteIntent()


    data class UpdateNote(val note:NoteEntity):NoteIntent()
}