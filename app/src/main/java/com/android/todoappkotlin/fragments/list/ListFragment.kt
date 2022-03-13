package com.android.todoappkotlin.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.viewmodel.SharedViewModel
import com.android.todoappkotlin.data.viewmodel.ToDoViewModel
import com.android.todoappkotlin.databinding.FragmentListBinding
import com.android.todoappkotlin.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

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

        sharedViewModel.hideKeyboard(requireActivity())
        setObserveToDoData()
        setRecyclerView()
        deleteTouch()

        sharedViewModel.isEmptyData.observe(viewLifecycleOwner){
            showEmptyDatabaseViews(it)
        }
        setHasOptionsMenu(true)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    private fun setObserveToDoData() {
        viewModel.getAllData.observe(viewLifecycleOwner) { list ->
            sharedViewModel.checkIfDatabaseEmpty(list)
            listAdapter.setData(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchTodo(searchQuery).observe(viewLifecycleOwner) {list ->
            list?.let {
                listAdapter.setData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> deleteDataAll()
            R.id.menu_priority_high -> viewModel.sortByHighPriority.observe(viewLifecycleOwner) { listAdapter.setData(it)}
            R.id.menu_priority_low -> viewModel.sortByLowPriority.observe(viewLifecycleOwner) { listAdapter.setData(it)}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteDataAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("SIM"){_, _ ->
            viewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Tarefas removida com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("Não"){_, _ -> }
        builder.setTitle("Remover tarefas")
        builder.setMessage("Deseja realmente remover todas as tarefas?")
        builder.create().show()
    }

    private fun deleteTouch() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val toDoData = listAdapter.dataList[position]
                viewModel.delete(toDoData)
                Snackbar.make(
                    binding.root,
                    "Tarefa '${toDoData.title}' removida com sucesso!",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Cancelar") {
                        viewModel.insert(toDoData)
                    }
                }.show()
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.recyclerView)
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

    private fun setRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 400
        }
    }
}