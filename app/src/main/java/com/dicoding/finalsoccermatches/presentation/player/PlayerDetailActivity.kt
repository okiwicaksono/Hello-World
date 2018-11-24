package com.dicoding.finalsoccermatches.presentation.player

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dicoding.finalsoccermatches.BuildConfig
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.data.SoccerRepositoryImpl
import com.dicoding.finalsoccermatches.domain.entity.Player
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.android.synthetic.main.activity_player_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.CoroutineContext

class PlayerDetailActivity : AppCompatActivity(), PlayerDetailContract.View, CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private lateinit var presenter: PlayerDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        job = Job()

        val playerId = intent.getStringExtra(getString(R.string.player_id))
        initPresenter(playerId)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initPresenter(playerId: String) {
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

        presenter = PlayerDetailPresenter(repository, this)
        presenter.loadPlayerDetail(playerId)
    }

    private fun initView() {
        launch {
            for (viewState in presenter.viewStates()) {
                runOnUiThread {
                    renderState(viewState)
                }
            }
        }
    }

    private fun setupPlayerToView(player: Player) {
        Glide.with(this).load(player.strFanart1).into(image)
        weight.text = player.strWeight
        height.text = player.strHeight
        position.text = player.strPosition
        description.text = player.strDescriptionEN
    }

    override fun renderState(viewState: PlayerDetailContract.ViewState) {
        when (viewState) {
            is PlayerDetailContract.ViewState.PlayerResultState -> {
                setupPlayerToView(viewState.player)
            }
            is PlayerDetailContract.ViewState.ErrorState -> {
                Log.e("error", viewState.error)
            }
        }
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
