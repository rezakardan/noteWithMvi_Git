package com.example.noteappwithmvii.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteappwithmvii.R
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.databinding.ActivityMainBinding
import com.example.noteappwithmvii.ui.note.NoteFragment
import com.example.noteappwithmvii.util.*
import com.example.noteappwithmvii.viewmodel.main.MainIntent
import com.example.noteappwithmvii.viewmodel.main.MainState
import com.example.noteappwithmvii.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: NoteAdapter
    @Inject
    lateinit var entity:NoteEntity

    private var selectedItem = 0
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.noteAppToolbar)

        binding.btnAdd.setOnClickListener {


            NoteFragment().show(supportFragmentManager, NoteFragment().tag)


        }


        viewModel.handleIntents(MainIntent.LoadAllNotes)

        lifecycleScope.launch {
            viewModel.state.collect { mainState ->

                when (mainState) {


                    is MainState.AllNotes -> {
                        binding.recycler.visibility = View.VISIBLE
                        binding.emptyLay.visibility = View.GONE

                        adapter.setData(mainState.note)
                        binding.recycler.layoutManager = LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )

                        binding.recycler.adapter = adapter
                    }

                    is MainState.EmptyList -> {

                        binding.recycler.visibility = View.GONE
                        binding.emptyLay.visibility = View.VISIBLE


                    }

                    is MainState.Delete->{}

                    is MainState.GoToNoteDetail->{


                        val noteFragment=NoteFragment()
                        val bundle=Bundle()
                        bundle.putInt(SEND_BUNDLE_ID,mainState.id)
                        noteFragment.arguments=bundle

                        noteFragment.show(supportFragmentManager,NoteFragment().tag)



                    }

                }


            }


        }



        adapter.setOnItemClickListener { noteEntity, state ->

            when(state){

                DELETE->{

                    entity.id=noteEntity.id
                    entity.desc=noteEntity.desc
                    entity.title=noteEntity.title
                    entity.category=noteEntity.category
                    entity.priority=noteEntity.priority

                    viewModel.handleIntents(MainIntent.DeleteNote(entity))



                }


                EDIT->{


                    viewModel.handleIntents(MainIntent.ClickOnNoteDetail(noteEntity.id))








                }




            }






        }




        binding.noteAppToolbar.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.actionFilter -> {

filterByPriority()
                    return@setOnMenuItemClickListener true
                }

                else -> {
                    return@setOnMenuItemClickListener false

                }


            }


        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_toolbar, menu)

        val search = menu.findItem(R.id.actionSearch)

        val searchView = search.actionView as SearchView

        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {


                viewModel.handleIntents(MainIntent.SearchNotes(newText))



                return true
            }


        })





        return super.onCreateOptionsMenu(menu)
    }


    private fun filterByPriority() {


        val builder = AlertDialog.Builder(this)

        val priority = arrayOf(ALL, HIGH, NORMAL, LOW)


        builder.setSingleChoiceItems(priority, selectedItem) { dialog, item ->

            when (item) {

                0 -> {
                    viewModel.handleIntents(MainIntent.LoadAllNotes)
                }

                in 1..3 -> {


                    viewModel.handleIntents(MainIntent.FilterByPriority(priority[item]))


                }


            }
            selectedItem = item

            dialog.dismiss()


        }

        val dialog: AlertDialog = builder.create()

        dialog.show()


    }


}