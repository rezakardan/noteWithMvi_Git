package com.example.noteappwithmvii.viewmodel.note

import com.example.noteappwithmvii.data.model.NoteEntity

sealed class NoteState {

    object Idle:NoteState()


data class SpinnersData(val categoryList:MutableList<String>,val priorityList:MutableList<String>):NoteState()


    data class SaveNote(val unit:Unit):NoteState()

    data class Error(val message:String):NoteState()

    data class NoteDetail(val note:NoteEntity):NoteState()

    data class UpdateNote(val unit:Unit):NoteState()

}