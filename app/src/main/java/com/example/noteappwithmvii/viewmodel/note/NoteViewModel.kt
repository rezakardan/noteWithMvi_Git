package com.example.noteappwithmvii.viewmodel.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.data.repository.NoteRepository
import com.example.noteappwithmvii.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

 val intent=Channel<NoteIntent>()

    private val _state=MutableStateFlow<NoteState>(NoteState.Idle)

    val state:StateFlow<NoteState>get() = _state

init {
    handleIntents()
}


  private  fun handleIntents()=viewModelScope.launch{


        intent.consumeAsFlow().collect{noteIntent->

            when(noteIntent){



                is NoteIntent.SpinnersData->{spinnersList()}

                is NoteIntent.SaveNote->{saveNotes(noteIntent.note)}

                is NoteIntent.DetailNote->{detailNote(noteIntent.id)}

                is NoteIntent.UpdateNote->{updateNote(noteIntent.note)}

            }



        }












    }

    private fun updateNote(note: NoteEntity)=viewModelScope.launch {

        _state.value=try {
            NoteState.UpdateNote(repository.updateNote(note))

        }catch (e:Exception){

            NoteState.Error(e.message.toString())
        }
    }

    private fun detailNote(id: Int)=viewModelScope.launch {
       val data=repository.getOneNote(id)
        data.collect{

            _state.value=NoteState.NoteDetail(it)




        }
    }

    private fun saveNotes(note: NoteEntity)=viewModelScope.launch {

        _state.value=try {

            NoteState.SaveNote(repository.saveNote(note))
        }catch (e:Exception){
            NoteState.Error(e.message.toString())
        }




    }

    private fun spinnersList() {
        val categoryList= mutableListOf(HOME, WORK, EDUCATION, HEALTH)

        val priorityList= mutableListOf(HIGH, NORMAL, LOW)


        _state.value=NoteState.SpinnersData(categoryList,priorityList)


    }


}