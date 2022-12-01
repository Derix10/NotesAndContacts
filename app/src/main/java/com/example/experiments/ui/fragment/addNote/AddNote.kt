package com.example.experiments.ui.fragment.addNote

import android.annotation.SuppressLint
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentAddNoteBinding
import com.example.experiments.model.NoteModel
import com.example.experiments.ui.App
import java.text.SimpleDateFormat
import java.util.*


class AddNote : BaseFragment<FragmentAddNoteBinding>(FragmentAddNoteBinding::inflate) {

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        super.setupObserver()
        if (arguments != null) {
            binding.edTitle.setText(arguments?.getString("title"))
            binding.edDescription.setText(arguments?.getString("description").toString())
            binding.btnSaveNote.text = "change"
        }
    }

    override fun setupUI() {
        binding.btnSaveNote.setOnClickListener {
            val id = arguments?.getInt("position")
            val title = binding.edTitle.text.toString()
            val date = getCurrentDateTime()
            val dateInString = date.toString("yyyy/MM/dd HH:mm")

            val description = binding.edDescription.text.toString()
            if (arguments != null) {
                App.db.noteDao()!!.upDateNote(
                    NoteModel(
                        id = id,
                        title = title,
                        description = description,
                        createdTime = dateInString))

            } else {
                App.db.noteDao()!!.addNote(
                    NoteModel(
                        title = title,
                        description = description,
                        createdTime = dateInString))
            }
            controller.navigate(R.id.noteFragment)
        }
    }

    private fun Date.toString(format : String, locale: Locale = Locale.getDefault()) : String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}

