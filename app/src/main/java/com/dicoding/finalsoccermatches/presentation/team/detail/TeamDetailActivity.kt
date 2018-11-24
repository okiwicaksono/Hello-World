package com.dicoding.finalsoccermatches.presentation.team.detail

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.R.drawable.ic_add_to_favorites
import com.dicoding.finalsoccermatches.R.drawable.ic_added_to_favorites
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.domain.entity.Team
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.external.database.TeamDatabase
import com.dicoding.finalsoccermatches.presentation.team.TeamContract
import com.dicoding.finalsoccermatches.presentation.team.TeamPresenter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.CoroutineContext

class TeamDetailActivity : AppCompatActivity(),
    TeamContract.View, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var presenter: TeamContract.Presenter
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var team: Team

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        job = Job()

        initPresenter()
        initView()

        val teamId = intent.getStringExtra(getString(R.string.team_id))
        checkFavoriteState(teamId)
        presenter.loadTeamDetails(teamId)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initPresenter() {
        if (this::presenter.isInitialized) return

        val objectMapper = ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .registerKotlinModule()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originRequest = chain.request()
                val originUrl = originRequest.url()

                val newUrl = originUrl.newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TSDB_API_KEY)
                    .addQueryParameter("language", "en-US")
                    .build()
                val newRequest = originRequest.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val soccerService = retrofit.create(SoccerService::class.java)
        val repository: SoccerRepository = SoccerRepositoryImpl(soccerService)

        presenter = TeamPresenter(repository, this)
    }

    private fun initView() {
        GlobalScope.launch {
            for (viewState in presenter.viewStates()) {
                runOnUiThread {
                    renderState(viewState)
                }
            }
        }
    }

    private fun setupView(team: Team) {
        this.team = team

        Glide.with(this).load(team.teamBadge).into(image)
        name.text = team.strTeam
        year.text = team.intFormedYear.toString()
        stadium.text = team.strStadium

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.overview)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.players)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewPager.adapter = TeamDetailAdapter(supportFragmentManager, team)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun renderState(viewState: TeamContract.ViewState) {
        when (viewState) {
            is TeamContract.ViewState.TeamDetailsResultState -> {
                val team = viewState.team
                setupView(team)
            }
            is TeamContract.ViewState.ErrorState -> {
                Log.e("error", viewState.error)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()
                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkFavoriteState(teamId: String) {
        try {
            val team = TeamDatabase(this).select(teamId.toInt())
            team?.let {
                isFavorite = true
            }
            setFavorite()
        } catch (e: Exception) {
            Snackbar.make(container, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setFavorite() {
        menuItem?.getItem(0)?.icon = if (isFavorite)
            ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            ContextCompat.getDrawable(this, ic_add_to_favorites)
    }

    private fun addToFavorite() {
        try {
            TeamDatabase(this).create(team)
            Snackbar.make(container, getString(R.string.added_to_favorite), Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Snackbar.make(container, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            team.idTeam.toInt().let { TeamDatabase(this).delete(it) }
            Snackbar.make(container, getString(R.string.remove_from_favorite), Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Snackbar.make(container, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}
