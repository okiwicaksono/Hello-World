package com.dicoding.finalsoccermatches.presentation.favorite

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class FavoriteParentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> FavoriteMatchesFragment()
        1 -> FavoriteTeamsFragment()
        else -> null
    }

    override fun getCount(): Int {
        return 2
    }
}