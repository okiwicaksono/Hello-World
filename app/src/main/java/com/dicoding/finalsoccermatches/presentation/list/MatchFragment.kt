package com.dicoding.finalsoccermatches.presentation.list

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.data.MatchRepository
import com.dicoding.finalsoccermatches.domain.data.MatchRepositoryImpl
import com.dicoding.finalsoccermatches.domain.entity.MatchType
import com.dicoding.finalsoccermatches.external.api.MatchService
import com.dicoding.finalsoccermatches.presentation.detail.DetailActivity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.fragment_match.*
import kotlinx.coroutines.experimental.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.experimental.CoroutineContext

class MatchFragment : Fragment(), MatchContract.View,
    SwipeRefreshLayout.OnRefreshListener, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var adapter: MatchAdapter
    private lateinit var presenter: MatchContract.Presenter

    companion object {
        private const val ARG_TYPE = "match_type"

        fun newInstance(matchType: MatchType): MatchFragment {
            val args = Bundle().also {
                it.putString(ARG_TYPE, matchType.type)
            }
            return MatchFragment().apply {
                arguments = args
            }
        }
    }

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
        return inflater.inflate(R.layout.fragment_match, container, false)
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

        val movieService = retrofit.create(MatchService::class.java)
        val repository: MatchRepository = MatchRepositoryImpl(movieService)

        presenter = MatchPresenter(repository, this)
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener(this)

        adapter = MatchAdapter { match ->
            startActivity<DetailActivity>(getString(R.string.event_id) to match.idEvent)
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

    override fun renderState(viewState: MatchContract.ViewState) {
        when (viewState) {
            is MatchContract.ViewState.LoadingState -> {
                swipeRefresh.isRefreshing = true
            }
            is MatchContract.ViewState.ResultState -> {
                swipeRefresh.isRefreshing = false
                adapter.submitList(viewState.matches)
            }
            is MatchContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                Toast.makeText(activity, viewState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRefresh() {
        activity?.let { activity ->
            arguments?.getString(ARG_TYPE).let {
                when (it) {
                    MatchType.LAST.type -> {
                        presenter.loadPastMatches()
                    }
                    MatchType.NEXT.type -> {
                        presenter.loadNextMatches()
                    }
                    MatchType.FAVORITE.type -> {
                        presenter.loadFavoriteMatches(activity)
                    }
                }
            }
        }
    }
}
