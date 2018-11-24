package com.dicoding.finalsoccermatches.presentation.team.detail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.dicoding.finalsoccermatches.domain.entity.Team

class TeamDetailAdapter(fm: FragmentManager, private val team: Team) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> OverviewFragment.newInstance(team.strDescriptionEN ?: "No description available.")
        1 -> PlayersFragment.newInstance(team.idTeam)
        else -> null
    }

    override fun getCount(): Int {
        return 2
    }
}