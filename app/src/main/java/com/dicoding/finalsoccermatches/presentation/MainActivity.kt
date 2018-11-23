package com.dicoding.finalsoccermatches.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.entity.MatchType
import com.dicoding.finalsoccermatches.presentation.match.MatchFragment
import com.dicoding.finalsoccermatches.presentation.match.MatchParentFragment
import com.dicoding.finalsoccermatches.presentation.search.MatchSearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.last_matches -> {
                    loadMatchParentFragment(savedInstanceState)
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
                .replace(
                    R.id.main_container,
                    MatchFragment.newInstance(matchType),
                    MatchFragment::class.java.simpleName
                )
                .commit()
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                startActivity<MatchSearchActivity>(getString(R.string.selected_item_id) to bottom_navigation.selectedItemId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}