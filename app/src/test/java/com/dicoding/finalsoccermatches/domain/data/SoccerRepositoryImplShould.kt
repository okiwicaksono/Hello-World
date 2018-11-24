package com.dicoding.finalsoccermatches.domain.data

import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.domain.entity.MatchResponse
import com.dicoding.finalsoccermatches.external.api.SoccerService
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner::class)
class SoccerRepositoryImplShould {

    private val service = mock(SoccerService::class.java)

    private val repository = SoccerRepositoryImpl(service)

    @Test
    fun return_match_when_get_past_matches_success() = runBlocking {
        val matches = listOf(
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

        val response =
            MatchResponse(matches = matches)

        whenever(service.getPastMatches(leagueId))
            .thenReturn(CompletableDeferred(response))

        val actualMatches = repository.getPastMatches(leagueId).await()

        assertEquals(matches.first(), actualMatches.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun throw_exception_when_get_past_matches_failed() = runBlocking {
        whenever(service.getPastMatches(leagueId))
            .thenThrow(IllegalArgumentException("error"))

        repository.getPastMatches(leagueId).await()

        Unit
    }

    @Test
    fun return_match_when_get_next_matches_success() = runBlocking {
        val matches = listOf(
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

        val response =
            MatchResponse(matches = matches)

        whenever(service.getPastMatches(leagueId))
            .thenReturn(CompletableDeferred(response))

        val actualMatches = repository.getPastMatches(leagueId).await()

        assertEquals(matches.first(), actualMatches.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun throw_exception_when_get_next_matches_failed() = runBlocking {
        whenever(service.getNextMatches(leagueId))
            .thenThrow(IllegalArgumentException("error"))

        repository.getNextMatches(leagueId).await()

        Unit
    }

    companion object {
        private const val leagueId = "4328"
    }

}