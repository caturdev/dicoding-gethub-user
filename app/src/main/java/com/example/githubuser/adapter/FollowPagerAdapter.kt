package com.example.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.FollowFragment

/**
 * Untuk Mengatur View Pager 2
 * ---------------------------
 * class ini untuk mengatur ViewPager2 dan komponen di dalamnya
 * apabila menerapkan di fragment maka AppCompatActivity diganti dengan FragemntActivity
 *
 *
 */
class FollowPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position + 1)
            putString(FollowFragment.GITHUB_USERNAME, username)
        }

        return fragment
    }
}