package com.example.myfirstsubmission.ui.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.databinding.FragmentFavoriteBinding
import com.example.myfirstsubmission.utils.EventAdapter
import com.example.myfirstsubmission.utils.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by lazy {
        obtainViewModel(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            setEventsData(events)
        }

        val layoutManager = LinearLayoutManager(context)
        binding.favoriteEventRv.layoutManager = layoutManager


        return root
    }

    override fun onAttach(context: Context) {
        favoriteViewModel.getFavoriteEvents()
        super.onAttach(context)
    }

    private fun obtainViewModel(activity: FragmentActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setEventsData(favoriteViewModel.favoriteEvents.value ?: emptyList())
    }

    private fun setEventsData(events: List<Event>) {
        Log.e("FavoriteFragment", "setEventsData: $events")
        val eventAdapter = EventAdapter(requireActivity().application, isFavoriteTab = true)
        eventAdapter.setEvents(events)
        binding.favoriteEventRv.adapter = eventAdapter

        if(events.isEmpty()){
            binding.favoriteEmpty.visibility = View.VISIBLE
        } else {
            binding.favoriteEmpty.visibility = View.GONE
        }
    }
}