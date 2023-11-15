package com.example.noteappwithmvii.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.noteappwithmvii.R
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.databinding.FragmentNoteBinding
import com.example.noteappwithmvii.util.EDIT
import com.example.noteappwithmvii.util.NEW
import com.example.noteappwithmvii.util.SEND_BUNDLE_ID
import com.example.noteappwithmvii.viewmodel.note.NoteIntent
import com.example.noteappwithmvii.viewmodel.note.NoteState
import com.example.noteappwithmvii.viewmodel.note.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentNoteBinding

    private val viewModel: NoteViewModel by viewModels()


    private var category = ""
    private var priority = ""

    private var noteId = 0
    private var type = ""

    private var priorityList: MutableList<String> = mutableListOf()

    private var categoryList: MutableList<String> = mutableListOf()

    @Inject
    lateinit var entity: NoteEntity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getInt(SEND_BUNDLE_ID) ?: 0


        type = if (noteId > 0) {

            EDIT

        } else {
            NEW
        }


        binding.closeImg.setOnClickListener { dismiss() }




        lifecycleScope.launch {


            viewModel.intent.send(NoteIntent.SpinnersData)

            if (type == EDIT) {

                viewModel.intent.send(NoteIntent.DetailNote(noteId))

            }

            viewModel.state.collect { noteState ->


                when (noteState) {

                    is NoteState.SpinnersData -> {

                        categorySpinner(noteState.categoryList)

                        prioritySpinner(noteState.priorityList)


                    }

                    is NoteState.SaveNote -> {
                        dismiss()
                    }

                    is NoteState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            noteState.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NoteState.Idle -> {}


                    is NoteState.NoteDetail -> {

                        binding.titleEdt.setText(noteState.note.title)
                        binding.descEdt.setText(noteState.note.desc)


                        binding.categoriesSpinner.setSelection(
                            getIndexed(
                                categoryList,
                                noteState.note.category
                            )
                        )


                        binding.prioritySpinner.setSelection(
                            getIndexed(
                                priorityList,
                                noteState.note.priority
                            )
                        )

                    }

                    is NoteState.UpdateNote -> {


                        dismiss()


                    }
                }


            }

        }

        binding.saveNote.setOnClickListener {

            val title = binding.titleEdt.text.toString()
            val desc = binding.descEdt.text.toString()

            entity.id = noteId
            entity.title = title
            entity.desc = desc
            entity.priority = priority
            entity.category = category

            lifecycleScope.launch {


                if (type == NEW) {

                    viewModel.intent.send(NoteIntent.SaveNote(entity))
                } else {

                    viewModel.intent.send(NoteIntent.UpdateNote(entity))
                }


            }


        }
    }


    private fun categorySpinner(list: MutableList<String>) {

        categoryList = list

        val adapter = ArrayAdapter(requireContext(), R.layout.itemspinner, categoryList)

        adapter.setDropDownViewResource(R.layout.item_spinner_list)
        binding.categoriesSpinner.adapter = adapter

        binding.categoriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    category = categoryList[p2]

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }


            }


    }


    private fun prioritySpinner(list: MutableList<String>) {

        priorityList = list

        val adapter = ArrayAdapter(requireContext(), R.layout.itemspinner, priorityList)

        adapter.setDropDownViewResource(R.layout.item_spinner_list)

        binding.prioritySpinner.adapter = adapter

        binding.prioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    priority = priorityList[p2]


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }


    }


    private fun getIndexed(list: List<String>, item: String): Int {

        var index = 0

        for (i in list.indices) {


            if (list[i] == item) {


                index = i
                break


            }


        }

        return index


    }


}


