package com.dicoding.finalsoccermatches.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.entity.MatchType
import com.dicoding.finalsoccermatches.presentation.favorite.FavoriteFragment
import com.dicoding.finalsoccermatches.presentation.match.MatchFragment
import com.dicoding.finalsoccermatches.presentation.match.MatchParentFragment
import com.dicoding.finalsoccermatches.presentation.search.MatchSearchActivity
import com.dicoding.finalsoccermatches.presentation.search.TeamSearchActivity
import com.dicoding.finalsoccermatches.presentation.team.TeamFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.matches -> {
                    loadMatchParentFragment(savedInstanceState)
                }
                R.id.teams -> {
                    loadTeamFragment(savedInstanceState)
                }
                R.id.favorites -> {
                    loadFavoriteFragment(savedInstanceState)
                }
            }
            true
        }
        bottom_navigation.selectedItemId = R.id.matches
    }

    private fun loadMatchParentFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    MatchParentFragment(),
                    MatchFragment::class.java.simpleName
                )
                .commit()
        }
    }

    private fun loadTeamFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    TeamFragment(),
                    MatchFragment::class.java.simpleName
                )
                .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.main_container,
                    FavoriteFragment(),
                    MatchFragment::class.java.simpleName
                )
                .commit()
        }
    }
}