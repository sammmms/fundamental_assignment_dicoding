package com.example.myfirstsubmission.ui.completed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.databinding.FragmentCompletedBinding
import com.example.myfirstsubmission.utils.EventAdapter
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class CompletedFragment : Fragment() {

    private var _binding: FragmentCompletedBinding? = null

    private lateinit var completedViewModel: CompletedViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        completedViewModel =
            ViewModelProvider(this)[CompletedViewModel::class.java]

        Log.e("", "complete fragment")

        _binding = FragmentCompletedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val searchBar : SearchBar = binding.searchBar
        val searchView : SearchView = binding.searchView

        searchView.setupWithSearchBar(searchBar)
        searchView.editText.setOnEditorActionListener{ _, _, _ ->
            searchBar.setText(searchView.text)
            searchView.hide()
            completedViewModel.loadEvents(searchQuery =  searchView.text.toString())
            false
        }


        val linearLayout = LinearLayoutManager(requireContext())
        val rv = binding.completedEventRv
        rv.layoutManager = linearLayout

        completedViewModel.completedEvents.observe(viewLifecycleOwner){
            events -> setEventsData(events)
        }

        completedViewModel.isLoading.observe(viewLifecycleOwner){
            loadingState -> setLoadingState(loadingState)
        }

        completedViewModel.isError.observe(viewLifecycleOwner){
            errorState -> setErrorState(errorState)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setEventsData(completedViewModel.completedEvents.value ?: listOf())
    }

    private fun setEventsData(events: List<Event>){
        val adapter = EventAdapter(requireActivity().application)
        adapter.setEvents(events)
        binding.completedEventRv.adapter = adapter

    }

    private fun setLoadingState(state: Boolean){
        binding.loadingProgress.visibility = if(state) View.VISIBLE else View.GONE
        binding.loadingText.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun setErrorState(state: Boolean){
        binding.errorText.visibility = if(state) View.VISIBLE else View.GONE
    }
}