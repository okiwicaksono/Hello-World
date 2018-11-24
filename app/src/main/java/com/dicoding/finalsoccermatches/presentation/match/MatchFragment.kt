package com.dicoding.finalsoccermatches.presentation.match

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.createCalendarIntent
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.domain.entity.League
import com.dicoding.finalsoccermatches.domain.entity.MatchType
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.parseToDesiredTimestamp
import com.dicoding.finalsoccermatches.presentation.match.detail.MatchDetailActivity
import com.dicoding.finalsoccermatches.presentation.search.MatchSearchActivity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.fragment_match.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class MatchFragment : Fragment(), MatchContract.View, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var adapter: MatchAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<League>
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.applicationContext?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, emptyArray<League>())
        }
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

        presenter = MatchPresenter(repository)
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener(this)

        adapter = MatchAdapter({ match ->
            startActivity<MatchDetailActivity>(getString(R.string.event_id) to match.idEvent)
        }, { match ->
            val title = match.strEvent
            val startTimeMillis = parseToDesiredTimestamp(match.dateEvent, match.strTime, 0)
            val endTimeMillis = parseToDesiredTimestamp(match.dateEvent, match.strTime, 1)
            startActivity(
                createCalendarIntent(
                    title = title,
                    startDateMillis = startTimeMillis,
                    endDateMillis = endTimeMillis,
                    description = title
                )
            )
        })

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
                startActivity<MatchSearchActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun renderState(viewState: MatchContract.ViewState) {
        when (viewState) {
            is MatchContract.ViewState.LoadingState -> {
                swipeRefresh.isRefreshing = true
            }
            is MatchContract.ViewState.LeagueResultState -> {
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
            is MatchContract.ViewState.MatchResultState -> {
                swipeRefresh.isRefreshing = false
                adapter.submitList(viewState.matches)
            }
            is MatchContract.ViewState.ErrorState -> {
                swipeRefresh.isRefreshing = false
                Log.e("error", viewState.error)
            }
        }
    }

    override fun onRefresh() {
        activity?.let {
            arguments?.getString(ARG_TYPE).let { matchType ->
                val leagueId = spinnerAdapter.getItem(spinner.selectedItemPosition)?.idLeague ?: "0"
                when (matchType) {
                    MatchType.LAST.type -> {
                        presenter.loadPastMatches(leagueId)
                    }
                    MatchType.NEXT.type -> {
                        presenter.loadNextMatches(leagueId)
                    }
                }
            }
        }
    }
}
