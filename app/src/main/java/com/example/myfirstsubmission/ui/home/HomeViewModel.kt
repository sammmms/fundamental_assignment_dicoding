package com.example.myfirstsubmission.ui.home

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

class HomeViewModel : ViewModel() {

    private val _activeEvents = MutableLiveData<List<Event>>()
    val activeEvents : LiveData<List<Event>> = _activeEvents

    private val _completedEvents = MutableLiveData<List<Event>>()
    val completedEvents : LiveData<List<Event>> = _completedEvents

    private val _isActiveEventsLoading = MutableLiveData<Boolean>()
    val isActiveEventsLoading: LiveData<Boolean> = _isActiveEventsLoading

    private val _isCompletedEventsLoading = MutableLiveData<Boolean>()
    val isCompletedEventsLoading: LiveData<Boolean> = _isCompletedEventsLoading

    private val _isActiveEventsError = MutableLiveData<Boolean>()
    val isActiveEventsError: LiveData<Boolean> = _isActiveEventsError

    private val _isCompletedEventsError = MutableLiveData<Boolean>()
    val isCompletedEventsError: LiveData<Boolean> = _isCompletedEventsError

    companion object{
        private const val TAG = "HomeViewModel"
    }

    init {
        loadActiveEvents()
        loadCompletedEvents()
    }

    private fun loadActiveEvents(){
        _isActiveEventsLoading.value = true
                val client = ApiConfig.getApiService().getActiveEvents(limit = "5")
                client.enqueue(object: Callback<ListResponse>{
                    override fun onResponse(
                        call: Call<ListResponse>,
                        response: Response<ListResponse>
                    ) {
                        _isActiveEventsLoading.value = false
                        if(response.isSuccessful){
                            _activeEvents.value = response.body()?.listEvents
                            _isActiveEventsError.value = false
                        } else {
                            _isActiveEventsError.value = true
                            Log.e(TAG, "onFailure : ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                        _isActiveEventsLoading.value = false
                        _isActiveEventsError.value = true
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })

    }

    private fun loadCompletedEvents(){
        _isCompletedEventsLoading.value = true
        runBlocking {
            val client = ApiConfig.getApiService().getCompletedEvents(limit = "5")
            client.enqueue(object: Callback<ListResponse>{
                override fun onResponse(
                    call: Call<ListResponse>,
                    response: Response<ListResponse>
                ) {
                    _isCompletedEventsLoading.value = false
                    if(response.isSuccessful){
                        _completedEvents.value = response.body()?.listEvents
                        _isCompletedEventsError.value = false
                    } else {
                        _isCompletedEventsError.value = true
                        Log.e(TAG, "onFailure : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                    _isCompletedEventsError.value = true
                    _isCompletedEventsLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }
}