package com.example.experiments.ui.fragment.note

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentNoteBinding
import com.example.experiments.model.NoteModel
import com.example.experiments.ui.App
import kotlin.collections.ArrayList


class NoteFragment : BaseFragment<FragmentNoteBinding>(FragmentNoteBinding::inflate),
    NoteAdapterLinearLayout.NoteListener, NoteAdapterGridLayout.NoteListener {
    private var adapter: NoteAdapterLinearLayout? = null
    private var adapterGrid: NoteAdapterGridLayout? = null

    private var trueOrFalse: Boolean? = null
    override fun setupUI() {

        adapter = NoteAdapterLinearLayout(this)
        adapterGrid = NoteAdapterGridLayout(this)
        binding.rcView.adapter = adapter
        loadNote()


        val swipeToDelete = object : SwipeToDelete() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition

                val builder = AlertDialog.Builder(activity)
                builder.run {
                    setTitle("Удалить эту заметку")
                    setMessage("Ты точно хочешь удалить?")
                    setPositiveButton("Да") { _, _ ->
                        adapter?.removeNote(itemPosition)
                    }
                    setNegativeButton("Нет") { _, _ ->
                        adapter?.notifyItemChanged(itemPosition)
                        adapterGrid?.notifyItemChanged(itemPosition)
                    }
                }

                alertDialog = builder.create()
                alertDialog?.show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.rcView)
    }

    private var alertDialog: AlertDialog? = null


    override fun onClickNote(model: NoteModel) {

        val bundle = Bundle()
        trueOrFalse = false
        val bb = arguments?.getSerializable("keyDate")
        Toast.makeText(requireContext(), "Its:$bb", Toast.LENGTH_SHORT).show()
        bundle.putString("keyDate", model.createdTime)
        bundle.putBoolean("keyForBoolean", trueOrFalse!!)
        bundle.putString("title", model.title)
        bundle.putString("description", model.description)
        bundle.putInt("position", model.id!!)
        controller.navigate(R.id.addContact, bundle)
    }

    override fun onClickGridLayout() {

        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter
    }

    override fun onClickGridReplaceDateOrAsc() {
        val builder = AlertDialog.Builder(activity)
        builder.run {
            setTitle("Отсортировать список")
            setMessage("Как ты хочешь отсортировать список")
            setPositiveButton("Алфавит") { _, _ ->
                adapterGrid?.setNote(
                    App.db.noteDao()
                        ?.getAllNoteAlphaphit() as ArrayList<NoteModel>
                )
            }
            setNegativeButton("Время") { _, _ ->
                adapterGrid?.setNote(App.db.noteDao()?.getAllNoteData() as ArrayList<NoteModel>)

            }
        }

        alertDialog = builder.create()
        alertDialog?.show()
    }

    override fun onClickLinearLayout() {

        binding.rcView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcView.adapter = adapterGrid

    }

    override fun onClickReplaceDateOrAsc() {
        val builder = AlertDialog.Builder(activity)
        builder.run {
            setTitle("Отсортировать список")
            setMessage("Как ты хочешь отсортировать список")
            setPositiveButton("Алфавит") { _, _ ->
                adapter?.setNote(
                    App.db.noteDao()
                        ?.getAllNoteAlphaphit() as ArrayList<NoteModel>
                )
            }
            setNegativeButton("Время") { _, _ ->
                adapter?.setNote(App.db.noteDao()?.getAllNoteData() as ArrayList<NoteModel>)

            }
        }

        alertDialog = builder.create()
        alertDialog?.show()
    }


    override fun setupObserver() {
        super.setupObserver()

        binding.btnAdd.setOnClickListener {

            controller.navigate(R.id.addContact)
        }
    }

    private fun loadNote() {
        val list = App.db.noteDao()?.getAllNote() as ArrayList<NoteModel>
        adapterGrid?.setNote(list)
        adapter?.setNote(list)
    }
}

