package com.android.todoappkotlin.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.model.ToDoData
import com.android.todoappkotlin.data.viewmodel.SharedViewModel
import com.android.todoappkotlin.data.viewmodel.ToDoViewModel
import com.android.todoappkotlin.databinding.FragmentAddBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.android.todoappkotlin.extensions.format
import com.android.todoappkotlin.extensions.text
import java.util.*

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.spinnerPriorities.onItemSelectedListener = sharedViewModel.listener
        dataPicker()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_check) {
            insertData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertData() {

        val textTitle = binding.edTitle.text.toString()
        val priority = binding.spinnerPriorities.selectedItem.toString()
        val description = binding.etDescription.text.toString()
        val date = binding.tilDate.text
        val hour = binding.tilHour.text
        val validation = sharedViewModel.verifyDataFormUser(textTitle, description, date, hour)
        if (validation) {
            val newData = ToDoData(
                id = 0,
                title = textTitle,
                priority = sharedViewModel.parsePriority(priority),
                description = description,
                date = date,
                hour = hour
            )
            viewModel.insert(newData)
            Toast.makeText(requireContext(), "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
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