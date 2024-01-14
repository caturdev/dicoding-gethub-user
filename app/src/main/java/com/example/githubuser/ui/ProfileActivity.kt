package com.example.githubuser.ui

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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

        binding.imageView.visibility = View.GONE

        // -----
        // Init Lottie Files
        //
        // block ini untuk melakukan init lottie files
        // untuk keperluan loading indicator
        // -----
        val githubLoading = binding.githubLoading
        githubLoading.setAnimationFromUrl("https://lottie.host/f4aa2a91-160f-40bf-927a-85ca4d9f1074/HesvD4FI65.json")
        
        // -----
        // Load View Model
        //
        // Section di bawah malakukan init view model
        // -----
        val profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ProfileViewModel::class.java]

        // -----
        // Get Parcelable
        //
        // Section di bawah untuk mengambil data yang dikirimkan dari main activity
        // yang berupa data detail user kedalam variabel githubUser
        // -----
        val githubUser = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<GithubUser>(GITHUB_USER, GithubUser::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<GithubUser>(GITHUB_USER)
        }

        // -----
        // Load User Followes/Following Data
        //
        // Section di bawah untuk mengambil data followes/following data
        // berdasarakan data suer yang dikirimkan dari main activity
        // dan diterima dari sestion sebelumnya
        // -----
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

    /**
     * Set User Data
     * -------------
     * Function untuk melakukan render data user pada sesi header component
     * data diambil dari proses get API github
     *
     *
     *
     * @param [GithubUserDetailResponse] user data
     * @return [Unit]
     */
    private fun setUserData(user: GithubUserDetailResponse): Unit {

        // menampilkan user avatar
        Glide
            .with(binding.imageView.context)
            .load(user.avatarUrl)
            .into(binding.imageView)

        binding.githubLoading.visibility = View.GONE
        binding.imageView.visibility = View.VISIBLE

        // show name data
        binding.name.text = user.name

        // show username data
        binding.username.text = "@${user.login}"

        // show location data
        binding.location.text = user.location

        // show company data
        binding.company.text = user.company

    }

    /**
     * On Option Item Selected
     * -----------------------
     * Function untuk menangani appbar action
     * salah satunya untuk menangani tombol back pada appbar
     *
     *
     *
     * @param [MenuItem] - object item yang meresponse
     * @return [Boolean]
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }
}