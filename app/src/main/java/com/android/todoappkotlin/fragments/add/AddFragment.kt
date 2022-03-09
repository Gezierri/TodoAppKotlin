package com.android.todoappkotlin.fragments.add

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.model.ToDoData
import com.android.todoappkotlin.data.viewmodel.SharedViewModel
import com.android.todoappkotlin.data.viewmodel.ToDoViewModel
import com.android.todoappkotlin.databinding.FragmentAddBinding

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
        val validation = sharedViewModel.verifyDataFormUser(textTitle, description)
        if (validation) {
            val newData = ToDoData(
                id = 0,
                title = textTitle,
                priority = sharedViewModel.parsePriority(priority),
                description = description
            )
            viewModel.insert(newData)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            sharedViewModel.hideKeyboard(requireActivity())
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please, fill out all fields.", Toast.LENGTH_SHORT).show()
        }

    }
}