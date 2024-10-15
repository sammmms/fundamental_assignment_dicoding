package com.example.myfirstsubmission.ui.completed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirstsubmission.data.retrofit.ApiConfig
import com.example.myfirstsubmission.data.retrofit.Event
import com.example.myfirstsubmission.data.retrofit.ListResponse
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletedViewModel : ViewModel() {

    private val _completedEvents = MutableLiveData<List<Event>>()
    val completedEvents : LiveData<List<Event>> = _completedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object{
        private const val TAG = "CompletedViewModel"
    }

    init {
        loadCompletedEvents()
    }

    private fun loadCompletedEvents(query: String = ""){
        _isLoading.value = true
        runBlocking {
            val client = ApiConfig.getApiService().getCompletedEvents(q = query)
            client.enqueue(object: Callback<ListResponse> {
                override fun onResponse(
                    call: Call<ListResponse>,
                    response: Response<ListResponse>
                ) {
                    _isLoading.value = false
                    if(response.isSuccessful){
                        _completedEvents.value = response.body()?.listEvents
                        _isError.value = false
                    } else {
                        _isError.value = true
                        Log.e(TAG, "onFailure : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                    _isError.value = true
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    fun loadEvents(searchQuery: String = ""){
        loadCompletedEvents(query = searchQuery)
    }

}