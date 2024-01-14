package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.GithubUserListAdapter
import com.example.githubuser.bloc.MainViewModel
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private val searchResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == SearchActivity.RESULT_CODE && result.data != null) {
            val resultValue = result.data?.getStringExtra(SearchActivity.EXTRA_SELECTED_VALUE)
            mainViewModel.getUsers(resultValue ?: "")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID = "sidiqpermana"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(
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

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_menu -> {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    searchResultLauncher.launch(intent)
                    true
                }

                else -> false
            }
        }

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