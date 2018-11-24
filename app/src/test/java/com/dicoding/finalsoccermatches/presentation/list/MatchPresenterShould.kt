package com.dicoding.finalsoccermatches.presentation.list

import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.presentation.match.MatchContract
import com.dicoding.finalsoccermatches.presentation.match.MatchPresenter
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when` as whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class MatchPresenterShould {

    private val repository = Mockito.mock(SoccerRepository::class.java)

    private val presenter = MatchPresenter(repository)

    @Test
    fun send_result_when_load_past_matches_success() = runBlocking {
        val matches = listOf(
            Match(
                idEvent = "576588",
                strHomeTeam = "Chelsea",
                strAwayTeam = "Everton",
                intHomeScore = "0",
                intAwayScore = "0",
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
                intHomeShots = "4",
                intAwayShots = "1",
                dateEvent = "2018-11-11",
                idHomeTeam = "133610",
                idAwayTeam = "133615"
            )
        )
        whenever(repository.getPastMatches())
            .thenReturn(CompletableDeferred(matches))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadPastMatches()
        }

        val actualStates = mutableListOf<MatchContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(MatchContract.ViewState.LoadingState, actualStates[0])
        assertTrue(actualStates[1] is MatchContract.ViewState.MatchResultState)
        assertEquals(matches.first(), (actualStates[1] as MatchContract.ViewState.MatchResultState).matches.first())
    }

    @Test
    fun send_error_state_when_load_past_matches_failed() = runBlocking {
        whenever(repository.getPastMatches())
            .thenThrow(IllegalArgumentException("error"))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadPastMatches()
        }

        val actualStates = mutableListOf<MatchContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(MatchContract.ViewState.LoadingState, actualStates[0])
        assertTrue(actualStates[1] is MatchContract.ViewState.ErrorState)
        assertEquals("error", (actualStates[1] as MatchContract.ViewState.ErrorState).error)
    }

    @Test
    fun send_result_when_load_next_matches_success() = runBlocking {
        val matches = listOf(
            Match(
                idEvent = "576590",
                strHomeTeam = "Tottenham",
                strAwayTeam = "Chelsea",
                intHomeScore = null,
                intAwayScore = null,
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
                intHomeShots = null,
                intAwayShots = null,
                dateEvent = "2018-11-24",
                idHomeTeam = "133616",
                idAwayTeam = "133610"
            )
        )
        whenever(repository.getNextMatches())
            .thenReturn(CompletableDeferred(matches))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadNextMatches()
        }

        val actualStates = mutableListOf<MatchContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(MatchContract.ViewState.LoadingState, actualStates[0])
        assertTrue(actualStates[1] is MatchContract.ViewState.MatchResultState)
        assertEquals(matches.first(), (actualStates[1] as MatchContract.ViewState.MatchResultState).matches.first())
    }

    @Test
    fun send_error_state_when_load_next_matches_failed() = runBlocking {
        whenever(repository.getNextMatches())
            .thenThrow(IllegalArgumentException("error"))

        val viewStateChannel = presenter.viewStates()
        launch {
            presenter.loadNextMatches()
        }

        val actualStates = mutableListOf<MatchContract.ViewState>()
        repeat(2) { actualStates.add(viewStateChannel.receive()) }

        assertEquals(MatchContract.ViewState.LoadingState, actualStates[0])
        assertTrue(actualStates[1] is MatchContract.ViewState.ErrorState)
        assertEquals("error", (actualStates[1] as MatchContract.ViewState.ErrorState).error)
    }
}