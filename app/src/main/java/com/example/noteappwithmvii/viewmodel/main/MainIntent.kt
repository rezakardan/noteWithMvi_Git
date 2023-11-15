package com.example.noteappwithmvii.viewmodel.main

import com.example.noteappwithmvii.data.model.NoteEntity

sealed class MainIntent {

object LoadAllNotes:MainIntent()

data class SearchNotes(val search:String):MainIntent()

data class FilterByPriority(val priority:String):MainIntent()


    data class DeleteNote(val note:NoteEntity):MainIntent()

    data class ClickOnNoteDetail(val id :Int):MainIntent()

}