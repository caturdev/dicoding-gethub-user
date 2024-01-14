package com.example.githubuser.ui

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.FollowPagerAdapter
import com.example.githubuser.bloc.ProfileViewModel
import com.example.githubuser.data.parcel.GithubUser
import com.example.githubuser.data.response.GithubUserDetailResponse
import com.example.githubuser.databinding.ActivityProfileBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var toolbar: Toolbar

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )

        const val GITHUB_USER = "github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        // -------------------------------
        // Mengambil data dari data parcel
        // -------------------------------
        val githubUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<GithubUser>(GITHUB_USER, GithubUser::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<GithubUser>(GITHUB_USER)
        }

        // -------------------------------
        // Block untuk menangani list view
        // -------------------------------
        profileViewModel.getUserDetail(githubUser?.username ?: "")
        profileViewModel.user.observe(this) { user ->
            setUserData(user)

            val viewPager: ViewPager2 = binding.viewPager

            val sectionPagerAdapter = FollowPagerAdapter(this)
            sectionPagerAdapter.username = githubUser?.username ?: ""

            viewPager.adapter = sectionPagerAdapter
            val tabs: TabLayout = binding.tabs

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Followers ${user.followers.toString()}"
                    1 -> "Following ${user.following.toString()}"
                    else -> ""
                }
            }.attach()
        }

        supportActionBar?.elevation = 0f

    }

    private fun setUserData(user: GithubUserDetailResponse) {

        // menampilkan user avatar
        Glide
            .with(binding.imageView.context)
            .load(user.avatarUrl)
            .into(binding.imageView)

        // show name data
        binding.name.text = user.name

        // show username data
        binding.username.text = "@${user.login}"

        // show location data
        binding.location.text = user.location

        // show company data
        binding.company.text = user.company

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }
}