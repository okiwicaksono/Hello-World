package com.dicoding.finalsoccermatches.presentation.favorite

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.presentation.team.TeamAdapter
import com.dicoding.finalsoccermatches.presentation.team.detail.TeamDetailActivity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.fragment_favorite_team.*
import kotlinx.coroutines.experimental.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.experimental.CoroutineContext

class FavoriteTeamsFragment : Fragment(), FavoriteContract.View, SwipeRefreshLayout.OnRefreshListener,
    CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var adapter: TeamAdapter
    private lateinit var presenter: FavoriteContract.Presenter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        job = Job()
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            initPresenter()
            initView()
            onRefresh()
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

        presenter = FavoritePresenter(repository, this)
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

    override fun renderState(viewState: FavoriteContract.ViewState) {
        when (viewState) {
            is FavoriteContract.ViewState.LoadingState -> {
                swipeRefresh.isRefreshing = true
            }
            is FavoriteContract.ViewState.TeamsResultState -> {
                swipeRefresh.isRefreshing = false
                adapter.submitList(viewState.teams)
            }
            is FavoriteContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                Log.e("error", viewState.error)
            }
        }
    }

    override fun onRefresh() {
        activity?.let {
            presenter.loadFavoriteTeams(it)
        }
    }
}
