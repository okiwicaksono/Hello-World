package com.dicoding.finalsoccermatches.external.data

import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.util.loadJSON
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class SoccerServiceShould {

    private val mapper = ObjectMapper()
        .registerKotlinModule()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    private val okHttpClient = OkHttpClient()

    private val webServer = MockWebServer()

    private lateinit var service: SoccerService

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(webServer.url(""))
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        service = retrofit.create(SoccerService::class.java)
    }

    @Test
    fun return_past_matches_on_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(loadJSON(this.javaClass, "json/past_matches_response.json"))
        webServer.enqueue(mockReponse)

        val expectedMatches = listOf(
            Match(
                idEvent = "576588",
                strHomeTeam = "Chelsea",
                strAwayTeam = "Everton",
                intHomeScore = 0,
                intAwayScore = 0,
                strHomeGoalDetails = "",
                strHomeLineupGoalkeeper = "Kepa Arrizabalaga; ",
                strHomeLineupDefense = "Cesar Azpilicueta; Antonio Ruediger; David Luiz; Marcos Alonso; ",
                strHomeLineupMidfield = "N'Golo Kante; Jorginho; Ross Barkley; ",
                strHomeLineupForward = "Willian; Alvaro Morata; Eden Hazard; ",
                strHomeLineupSubstitutes = "Wilfredo Caballero; Andreas Christensen; Davide Zappacosta; Cesc Fabregas; Ross Barkley; Pedro Rodriguez; Olivier Giroud; ",
                strAwayGoalDetails = "",
                strAwayLineupGoalkeeper = "Jordan Pickford; ",
                strAwayLineupDefense = "Seamus Coleman; Michael Keane; Yerry Mina; Lucas Digne; ",
                strAwayLineupMidfield = "Andre Gomes; Idrissa Gana Gueye; Theo Walcott; Gylfi Sigurdsson; Bernard; ",
                strAwayLineupForward = "Richarlison; ",
                strAwayLineupSubstitutes = "Maarten Stekelenburg; Leighton Baines; Phil Jagielka; Tom Davies; Ademola Lookman; Dominic Calvert-Lewin; Cenk Tosun; ",
                intHomeShots = 4,
                intAwayShots = 1,
                dateEvent = "2018-11-11",
                idHomeTeam = "133610",
                idAwayTeam = "133615"
            )
        )

        val actualMatches = service.getPastMatches(leagueId).await().matches
        assertEquals(expectedMatches.first(), actualMatches.first())
    }

    @Test(expected = HttpException::class)
    fun throw_http_exception_when_get_past_matches_on_non_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(403)
        webServer.enqueue(mockReponse)

        service.getPastMatches(leagueId).await()

        Unit // to hide warning, because test case should return unit
    }

    @Test
    fun return_next_matches_on_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(200)
            .setBody(loadJSON(this.javaClass, "json/next_matches_response.json"))
        webServer.enqueue(mockReponse)

        val expectedMatches = listOf(
            Match(
                idEvent = "576590",
                strHomeTeam = "Tottenham",
                strAwayTeam = "Chelsea",
                intHomeScore = 0,
                intAwayScore = 0,
                strHomeGoalDetails = null,
                strHomeLineupGoalkeeper = null,
                strHomeLineupDefense = null,
                strHomeLineupMidfield = null,
                strHomeLineupForward = null,
                strHomeLineupSubstitutes = null,
                strAwayGoalDetails = null,
                strAwayLineupGoalkeeper = null,
                strAwayLineupDefense = null,
                strAwayLineupMidfield = null,
                strAwayLineupForward = null,
                strAwayLineupSubstitutes = null,
                intHomeShots = 0,
                intAwayShots = 0,
                dateEvent = "2018-11-24",
                idHomeTeam = "133616",
                idAwayTeam = "133610"
            )
        )

        val actualMatches = service.getNextMatches(leagueId).await().matches
        assertEquals(expectedMatches.first(), actualMatches.first())
    }

    @Test(expected = HttpException::class)
    fun throw_http_exception_when_get_next_matches_on_non_200_http_response() = runBlocking {
        val mockReponse = MockResponse()
            .setResponseCode(403)
        webServer.enqueue(mockReponse)

        service.getNextMatches(leagueId).await()

        Unit // to hide warning, because test case should return unit
    }

    companion object {
        const val leagueId = "4328"
    }
}