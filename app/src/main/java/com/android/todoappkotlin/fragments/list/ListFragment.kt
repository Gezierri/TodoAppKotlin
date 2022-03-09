package com.android.todoappkotlin.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.viewmodel.SharedViewModel
import com.android.todoappkotlin.data.viewmodel.ToDoViewModel
import com.android.todoappkotlin.databinding.FragmentListBinding
import com.android.todoappkotlin.fragments.list.adapter.ListAdapter


class ListFragment : Fragment() {

    private  lateinit var binding: FragmentListBinding
    private val listAdapter: ListAdapter by lazy { ListAdapter() }
    private val viewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getAllData.observe(viewLifecycleOwner) { list ->
            sharedViewModel.checkIfDatabaseEmpty(list)
            listAdapter.submitList(list)
        }
        sharedViewModel.isEmptyData.observe(viewLifecycleOwner){
            showEmptyDatabaseViews(it)
        }
        setHasOptionsMenu(true)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (!emptyDatabase){
            binding.ivNoTask.visibility = View.GONE
            binding.tvNoTask.visibility = View.GONE
        }else{
            binding.ivNoTask.visibility = View.VISIBLE
            binding.tvNoTask.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete_all){
            deleteDataAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDataAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            viewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No"){_, _ -> }
        builder.setTitle("Delete all")
        builder.setMessage("Are you sure you want to remove all?")
        builder.create().show()
    }
}