package com.android.todoappkotlin.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.model.ToDoData
import com.android.todoappkotlin.data.viewmodel.SharedViewModel
import com.android.todoappkotlin.data.viewmodel.ToDoViewModel
import com.android.todoappkotlin.databinding.FragmentUpdateBinding
import com.android.todoappkotlin.extensions.format
import com.android.todoappkotlin.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val viewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        binding.edTitle.setText(args.currentItem.title)
        binding.etDescription.setText(args.currentItem.description)
        binding.tieDate.setText(args.currentItem.date)
        binding.tieHour.setText(args.currentItem.hour)
        binding.spinnerPriorities.setSelection(sharedViewModel.setPriority(args.currentItem.priority))
        binding.spinnerPriorities.onItemSelectedListener = sharedViewModel.listener

        dataPicker()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_update -> updateData()
            R.id.menu_delete -> deleteData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteData() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Sim"){_, _ ->
            viewModel.delete(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Tarefa '${args.currentItem.title}' removida com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Não"){_, _ -> }
        builder.setTitle("Remover '${args.currentItem.title}'")
        builder.setMessage("Deseja realmente remover '${args.currentItem.title}'?")
        builder.create().show()
    }

    private fun updateData() {

        val title = binding.edTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val priority = binding.spinnerPriorities.selectedItem.toString()
        val date = binding.tilDate.text
        val hour = binding.tilHour.text
        val validation = sharedViewModel.verifyDataFormUser(title, description, date, hour)

        if (validation) {
            val updateData = ToDoData(
                id = args.currentItem.id,
                title = title,
                description = description,
                priority = sharedViewModel.parsePriority(priority),
                date = date,
                hour = hour
            )
            viewModel.update(updateData)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Tarefa alterada com sucesso!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun dataPicker() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(parentFragmentManager, "")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(parentFragmentManager, null)
        }
    }
}