package com.dicoding.finalsoccermatches.presentation.match

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dicoding.finalsoccermatches.domain.entity.MatchType

class MatchParentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> MatchFragment.newInstance(MatchType.LAST)
        1 -> MatchFragment.newInstance(MatchType.NEXT)
        else -> null
    }

    override fun getCount(): Int {
        return 2
    }
}