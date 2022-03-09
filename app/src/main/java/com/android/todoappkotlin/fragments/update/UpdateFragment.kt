package com.android.todoappkotlin.fragments.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
        binding.spinnerPriorities.setSelection(sharedViewModel.setPriority(args.currentItem.priority))
        binding.spinnerPriorities.onItemSelectedListener = sharedViewModel.listener
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
        builder.setPositiveButton("Yes"){_, _ ->
            viewModel.delete(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed: '${args.currentItem.title}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        builder.create().show()
    }

    private fun updateData() {
        val title = binding.edTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val priority = binding.spinnerPriorities.selectedItem.toString()

        val validation = sharedViewModel.verifyDataFormUser(title, description)

        if (validation) {
            val updateData = ToDoData(
                id = args.currentItem.id,
                title = title,
                description = description,
                priority = sharedViewModel.parsePriority(priority)
            )
            viewModel.update(updateData)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }
}