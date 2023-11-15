package com.example.noteappwithmvii.viewmodel.main

import com.example.noteappwithmvii.data.model.NoteEntity

sealed class MainState {

data class AllNotes( val note:List<NoteEntity>):MainState()

object EmptyList:MainState()


    data class Delete(val unit:Unit):MainState()

    data class GoToNoteDetail(val id:Int):MainState()

}