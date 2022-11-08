package com.example.experiments.ui.fragment.note

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentNoteBinding
import com.example.experiments.model.NoteModel
import com.example.experiments.ui.App
import com.example.experiments.ui.fragment.addNote.AddNote


class NoteFragment : BaseFragment<FragmentNoteBinding>(FragmentNoteBinding::inflate), NoteAdapter.NoteListener {
   private var adapter: NoteAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    override fun setupUI() {
        adapter = NoteAdapter(this)
        binding.rcView.adapter = adapter
        loadNote()




        val swipeToDelete = object : SwipeToDelete() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition

                val builder = AlertDialog.Builder(activity)
                builder.run {
                    setTitle("Delete project list")
                    setMessage("You want to delete project?")
                    setPositiveButton("Yes") { _, _ ->   adapter?.removeNote(itemPosition)                }
                    setNegativeButton("Cancel") { _, _ -> adapter?.notifyItemChanged(itemPosition)
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
        val fragment = AddNote()
        val bundle = Bundle()
        bundle.putString("title", model.title)
        bundle.putString("description", model.description)
        bundle.putInt("position", model.id!!)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit()

    }



    override fun setupObserver() {
        super.setupObserver()
        binding.btnAdd.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container,AddNote()).addToBackStack(null).commit()
        }
    }
    private fun loadNote() {

            adapter?.setNote(App.db.noteDao()!!.getAllNote() as ArrayList<NoteModel>)

        }


}

