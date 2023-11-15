package com.example.noteappwithmvii.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappwithmvii.R
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.databinding.ItemNoteBinding
import com.example.noteappwithmvii.util.*
import javax.inject.Inject

class NoteAdapter @Inject constructor() : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var noteList = emptyList<NoteEntity>()

    lateinit var context: Context

    lateinit var binding: ItemNoteBinding

    inner class NoteViewHolder(item: View) : RecyclerView.ViewHolder(item) {


        fun onBind(oneItem: NoteEntity) {

            binding.titleTxt.text = oneItem.title
            binding.descTxt.text = oneItem.desc



            when (oneItem.category) {


                HEALTH -> {

                    binding.categoryImg.setImageResource(R.drawable.healthcare)


                }

                WORK -> {
                    binding.categoryImg.setImageResource(R.drawable.work)
                }


                EDUCATION -> {
                    binding.categoryImg.setImageResource(R.drawable.education)
                }

                HOME -> {
                    binding.categoryImg.setImageResource(R.drawable.home)
                }


            }


            when (oneItem.priority) {

                HIGH -> {
                    binding.priorityColor.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    )
                }
                NORMAL -> {
                    binding.priorityColor.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.yellow
                        )
                    )
                }
                LOW -> {
                    binding.priorityColor.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.aqua
                        )
                    )
                }


            }

            binding.menuImg.setOnClickListener {

                val popUpMenu = PopupMenu(context, it)
                popUpMenu.menuInflater.inflate(R.menu.menu_popup, popUpMenu.menu)


                popUpMenu.show()


                popUpMenu.setOnMenuItemClickListener { menuItem ->

                    when (menuItem.itemId) {


                        R.id.edit -> {

                            onItemClick?.let {

                                it(oneItem, EDIT)

                            }


                        }


                        R.id.delete -> {
                            onItemClick?.let {
                                it(oneItem, DELETE)
                            }
                        }


                    }



                    return@setOnMenuItemClickListener true
                }


            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        context = parent.context

        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.onBind(noteList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = noteList.size


    private var onItemClick: ((NoteEntity, String) -> Unit)? = null


    fun setOnItemClickListener(listener: (NoteEntity, String) -> Unit) {

        onItemClick = listener


    }


    fun setData(data: List<NoteEntity>) {

        val noteDiff = NoteDiffUtils(noteList, data)

        val diff = DiffUtil.calculateDiff(noteDiff)

        noteList = data

        diff.dispatchUpdatesTo(this)


    }


    class NoteDiffUtils(val oldItem: List<NoteEntity>, val newItem: List<NoteEntity>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }


    }


}