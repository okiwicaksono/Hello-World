package com.dicoding.finalsoccermatches.presentation.match.detail

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.finalsoccermatches.*
import com.dicoding.finalsoccermatches.R.drawable.ic_add_to_favorites
import com.dicoding.finalsoccermatches.R.drawable.ic_added_to_favorites
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.external.database.MatchDatabase
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.CoroutineContext

class MatchDetailActivity : AppCompatActivity(), MatchDetailContract.View, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var presenter: MatchDetailContract.Presenter
    private lateinit var match: Match

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        job = Job()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getStringExtra(getString(R.string.event_id))

        checkFavoriteState(eventId)
        initPresenter(eventId)

        launch {
            for (viewState in presenter.viewStates()) {
                runOnUiThread {
                    renderState(viewState)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun renderState(viewState: MatchDetailContract.ViewState) {
        when (viewState) {
            is MatchDetailContract.ViewState.LoadingState -> {
                swipeRefresh.isRefreshing = true
            }
            is MatchDetailContract.ViewState.MatchResultState -> {
                swipeRefresh.isRefreshing = false
                setupMatchToView(viewState.match)
            }
            is MatchDetailContract.ViewState.URLResultState -> {
                swipeRefresh.isRefreshing = false
                Glide.with(this).load(viewState.imageUrl).into(viewState.imageView)
            }
            is MatchDetailContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                Toast.makeText(this, viewState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPresenter(eventId: String) {
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

        presenter = MatchDetailPresenter(repository, this)
        presenter.loadMatchDetail(eventId)
    }

    private fun setupMatchToView(match: Match) {
        this.match = match

        date.text = parseToDesiredDate(dateString = match.dateEvent, timeString = match.strTime)
        time.text = parseToDesiredTime(dateString = match.dateEvent, timeString = match.strTime)
        home_score.text = match.intHomeScore
        away_score.text = match.intAwayScore
        home_team.text = match.strHomeTeam
        away_team.text = match.strAwayTeam
        home_goals.text = match.strHomeGoalDetails?.let { formatPlayerList(it) }
        away_goals.text = match.strAwayGoalDetails?.let { formatPlayerList(it) }
        home_shots.text = match.intHomeShots
        away_shots.text = match.intAwayShots
        home_goalkeeper.text = match.strHomeLineupGoalkeeper
        away_goalkeeper.text = match.strAwayLineupGoalkeeper
        home_defenders.text = match.strHomeLineupDefense?.let { formatPlayerList(it) }
        away_defenders.text = match.strAwayLineupDefense?.let { formatPlayerList(it) }
        home_midfielders.text = match.strHomeLineupMidfield?.let { formatPlayerList(it) }
        away_midfielders.text = match.strAwayLineupMidfield?.let { formatPlayerList(it) }
        home_strikers.text = match.strHomeLineupForward?.let { formatPlayerList(it) }
        away_strikers.text = match.strAwayLineupForward?.let { formatPlayerList(it) }
        home_substitutes.text = match.strHomeLineupSubstitutes?.let { formatPlayerList(it) }
        away_substitutes.text = match.strAwayLineupSubstitutes?.let { formatPlayerList(it) }

        match.idHomeTeam.let { presenter.loadTeamBadge(it, home_badge) }
        match.idAwayTeam.let { presenter.loadTeamBadge(it, away_badge) }
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

    private fun checkFavoriteState(eventId: String) {
        try {
            val match = MatchDatabase(this).select(eventId.toInt())
            match?.let {
                isFavorite = true
            }
            setFavorite()
        } catch (e: SQLiteConstraintException) {
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
            MatchDatabase(this).create(match)
            Snackbar.make(container, getString(R.string.added_to_favorite), Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(container, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            match.idEvent.toInt().let { MatchDatabase(this).delete(it) }
            Snackbar.make(container, getString(R.string.remove_from_favorite), Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(container, e.localizedMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}
