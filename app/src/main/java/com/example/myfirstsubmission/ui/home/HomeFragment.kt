package com.example.myfirstsubmission.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.databinding.FragmentHomeBinding
import com.example.myfirstsubmission.utils.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set layout to horizontal for active events
        val activeLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val activeRv = binding.activeEventRv
        activeRv.layoutManager = activeLayoutManager

        val completedLayoutManager = LinearLayoutManager(requireContext())
        val completedRv = binding.completedEventRv
        completedRv.layoutManager = completedLayoutManager

        homeViewModel.activeEvents.observe(viewLifecycleOwner){
            events -> setEventsData(events, isActive = true)
        }

        homeViewModel.completedEvents.observe(viewLifecycleOwner){
            events -> setEventsData(events, isActive = false)
        }

        homeViewModel.isActiveEventsLoading.observe(viewLifecycleOwner){
            isLoading -> setLoadingState(isLoading, isActive = true)
        }

        homeViewModel.isCompletedEventsLoading.observe(viewLifecycleOwner){
            isLoading -> setLoadingState(isLoading, isActive = false)
        }

        homeViewModel.isActiveEventsError.observe(viewLifecycleOwner){
            isError -> setErrorState(isError, isActive = true)
        }

        homeViewModel.isCompletedEventsError.observe(viewLifecycleOwner){
            isError -> setErrorState(isError, isActive = false)
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        setEventsData(homeViewModel.activeEvents.value ?: emptyList(), isActive = true)
        setEventsData(homeViewModel.completedEvents.value ?: emptyList(), isActive = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEventsData(events: List<Event>, isActive: Boolean){
        val adapter = EventAdapter(requireActivity().application)
        adapter.setEvents(events)
        if(isActive){
            binding.activeEventRv.adapter = adapter
        } else {
            binding.completedEventRv.adapter = adapter
        }

    }

    private fun setLoadingState(state: Boolean, isActive: Boolean){
        val progress = if(isActive) binding.loadingProgressActive else binding.loadingProgressCompleted
        val textProgress = if(isActive) binding.loadingTextActive else binding.loadingTextCompleted

        progress.visibility = if(state) View.VISIBLE else View.GONE
        textProgress.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun setErrorState(state: Boolean, isActive: Boolean){
        val errorText = if(isActive) binding.activeEventError else binding.completedEventError

        errorText.visibility = if(state) View.VISIBLE else View.GONE
    }

}