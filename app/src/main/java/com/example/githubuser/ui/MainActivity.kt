package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.githubuser.adapter.GithubUserListAdapter
import com.example.githubuser.bloc.MainViewModel
import com.example.githubuser.data.response.GithubUsersResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID = "sidiqpermana"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        // -------------------------------
        // Block untuk menangani list view
        // -------------------------------
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.users.observe(this) { user -> setUserListData(user) }

        // -------------------------------
        // Block untuk menangani loading indicator
        // -------------------------------
        mainViewModel.isLoading.observe(this) { isLoading -> showLoading(isLoading) }

        // -------------------------------
        // Block untuk lottie animation
        // -------------------------------
        val githubLoading = binding.githubLoading
        githubLoading.setAnimationFromUrl("https://lottie.host/f4aa2a91-160f-40bf-927a-85ca4d9f1074/HesvD4FI65.json")
    }

    private fun setUserListData(user: List<ItemsItem>) {
        val adapter = GithubUserListAdapter()
        adapter.submitList(user)
        binding.rvUser.adapter = adapter
    }

    // function untuk menampilkan atau menyembunyikan loading indicator
    private fun showLoading(b: Boolean) {
        if (b) {
            binding.githubLoading.visibility = View.VISIBLE
        } else {
            binding.githubLoading.visibility = View.GONE
        }
    }

}