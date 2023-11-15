package com.example.noteappwithmvii.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

private val _state=MutableStateFlow<MainState>(MainState.EmptyList)


    val state:StateFlow<MainState>get() = _state



    fun handleIntents(intent:MainIntent){

        when(intent){

           is MainIntent.LoadAllNotes->{getAllNotes()}

is MainIntent.SearchNotes->{searchNotes(intent.search)}

is MainIntent.FilterByPriority->{(filterByPriority(intent.priority))}


            is MainIntent.DeleteNote->{deleteNote(intent.note)}

            is MainIntent.ClickOnNoteDetail->{goToNoteDetail(intent.id)}
        }





    }

    private fun goToNoteDetail(id: Int)=viewModelScope.launch {


            _state.value=MainState.GoToNoteDetail(id)





    }

    private fun deleteNote(note: NoteEntity)=viewModelScope.launch {

        _state.value=MainState.Delete(repository.deleteNote(note))



    }

    private fun filterByPriority(priority: String) =viewModelScope.launch{

        val data=repository.filterByPriority(priority)
        data.collect{

            if (it.isNotEmpty()){

                _state.value=MainState.AllNotes(it)

            }else{
                _state.value=MainState.EmptyList
            }



        }




    }

    private fun searchNotes(search: String) =viewModelScope.launch{
        val data=repository.searchNotes(search)

        data.collect{

            if (it.isNotEmpty()){
                _state.value=MainState.AllNotes(it)
            }else{
                _state.value=MainState.EmptyList
            }


        }
    }

    private fun getAllNotes()=viewModelScope.launch {

        val data=repository.allNotes()

        data.collect{

            if (it.isNotEmpty()){


            _state.value=MainState.AllNotes(it)
            }else{

                _state.value=MainState.EmptyList
            }

        }
    }


}