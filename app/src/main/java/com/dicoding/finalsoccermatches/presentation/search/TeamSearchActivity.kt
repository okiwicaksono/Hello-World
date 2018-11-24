package com.dicoding.finalsoccermatches.presentation.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.invisible
import com.dicoding.finalsoccermatches.presentation.team.TeamAdapter
import com.dicoding.finalsoccermatches.presentation.team.TeamContract
import com.dicoding.finalsoccermatches.presentation.team.TeamPresenter
import com.dicoding.finalsoccermatches.presentation.team.detail.TeamDetailActivity
import com.dicoding.finalsoccermatches.visible
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_team_search.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.startActivity
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.CoroutineContext

class TeamSearchActivity : AppCompatActivity(), TeamContract.View,
    SwipeRefreshLayout.OnRefreshListener, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var adapter: TeamAdapter
    private lateinit var presenter: TeamContract.Presenter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_search)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initPresenter()
        initView()

        emptyView.visible()
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
        swipeRefresh.setOnRefreshListener(this)

        adapter = TeamAdapter { team ->
            startActivity<TeamDetailActivity>(getString(R.string.team_id) to team.idTeam)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        GlobalScope.launch {
            for (viewState in presenter.viewStates()) {
                runOnUiThread {
                    renderState(viewState)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @SuppressLint("SetTextI18n")
    override fun renderState(viewState: TeamContract.ViewState) {
        when (viewState) {
            is TeamContract.ViewState.LoadingState -> {
                swipeRefresh.isRefreshing = true
            }
            is TeamContract.ViewState.TeamsResultState -> {
                swipeRefresh.isRefreshing = false
                if (viewState.teams.isEmpty()) {
                    emptyView.text = "No data for ${searchView.query}"
                } else {
                    adapter.submitList(viewState.teams)
                    emptyView.invisible()
                }
            }
            is TeamContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                emptyView.text = "No data for ${searchView.query.trim()}"
                Log.e("error", viewState.error)
            }
        }
    }

    override fun onRefresh() {
        val query = searchView.query.toString().trim().replace(" ", "_")
        if (query.isNotEmpty()) {
            presenter.loadTeamsByKeyword(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchItem.expandActionView()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                onRefresh()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
