package com.dicoding.finalsoccermatches.presentation.team

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.domain.entity.League
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.presentation.search.TeamSearchActivity
import com.dicoding.finalsoccermatches.presentation.team.detail.TeamDetailActivity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.fragment_team.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.CoroutineContext

class TeamFragment : Fragment(), TeamContract.View,
    SwipeRefreshLayout.OnRefreshListener, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var adapter: TeamAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<League>
    private lateinit var presenter: TeamContract.Presenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        job = Job()
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            initPresenter()
            initView()
            presenter.loadAllLeagues()
        }
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

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        GlobalScope.launch {
            for (viewState in presenter.viewStates()) {
                activity?.runOnUiThread {
                    renderState(viewState)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                startActivity<TeamSearchActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun renderState(viewState: TeamContract.ViewState) {
        when (viewState) {
            is TeamContract.ViewState.LoadingState -> {
                swipeRefresh.isRefreshing = true
            }
            is TeamContract.ViewState.LeagueResultState -> {
                activity?.applicationContext?.let {
                    spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, viewState.leagues)
                    spinner.adapter = spinnerAdapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            onRefresh()
                        }
                    }
                }
            }
            is TeamContract.ViewState.TeamsResultState -> {
                swipeRefresh.isRefreshing = false
                adapter.submitList(viewState.teams)
            }
            is TeamContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                Toast.makeText(activity, viewState.error, Toast.LENGTH_SHORT).show()
                Log.e("error", viewState.error)
            }
        }
    }

    override fun onRefresh() {
        val leagueId = spinnerAdapter.getItem(spinner.selectedItemPosition)?.idLeague ?: "0"
        presenter.loadTeams(leagueId)
    }
}