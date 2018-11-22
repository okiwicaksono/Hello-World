package com.dicoding.finalsoccermatches.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.entity.MatchType
import com.dicoding.finalsoccermatches.presentation.list.MatchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.last_matches -> {
                    loadMatchFragment(savedInstanceState, MatchType.LAST)
                }
                R.id.next_matches -> {
                    loadMatchFragment(savedInstanceState, MatchType.NEXT)
                }
                R.id.favorites -> {
                    loadMatchFragment(savedInstanceState, MatchType.FAVORITE)
                }
            }
            true
        }
        bottom_navigation.selectedItemId = R.id.last_matches
    }

    private fun loadMatchFragment(savedInstanceState: Bundle?, matchType: MatchType) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, MatchFragment.newInstance(matchType), MatchFragment::class.java.simpleName)
                .commit()
        }
    }
}