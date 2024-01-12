package com.example.githubuser.bloc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubUsersResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
        private const val USER_ID = "sidiqpermana"
    }

    private val _users = MutableLiveData<List<ItemsItem>>()
    val users: LiveData<List<ItemsItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUsers()
    }

    private fun getUsers() {
        // menampilkan loading indicator
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUser(USER_ID)
        client.enqueue(object : Callback<GithubUsersResponse> {

            // response handler untuk response
            override fun onResponse(
                call: Call<GithubUsersResponse>,
                response: Response<GithubUsersResponse>
            ) {

                // menyembunyikan loading indicator
                _isLoading.value = false
                Log.e(MainViewModel.TAG, "Res: $response")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody.items
                    }
                } else {
                    Log.e(MainViewModel.TAG, "Error: ${response.message()}")
                }

            }

            // response handler saat terjadi gagal koneksi
            override fun onFailure(call: Call<GithubUsersResponse>, t: Throwable) {

                // menyembunyikan loading indicator
                _isLoading.value = false

            }

        })
    }

}