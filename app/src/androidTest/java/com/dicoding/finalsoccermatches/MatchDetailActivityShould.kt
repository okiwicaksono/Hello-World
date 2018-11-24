package com.dicoding.finalsoccermatches

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.presentation.match.detail.MatchDetailActivity
import com.dicoding.finalsoccermatches.presentation.match.detail.MatchDetailContract
import kotlinx.coroutines.experimental.channels.Channel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as whenever

@RunWith(AndroidJUnit4::class)
class MatchDetailActivityShould {

    private val presenter = Mockito.mock(MatchDetailContract.Presenter::class.java)

    @get:Rule
    val rule = object : ActivityTestRule<MatchDetailActivity>(MatchDetailActivity::class.java) {
        override fun beforeActivityLaunched() {
            whenever(presenter.viewStates())
                .thenReturn(Channel()) // it is verify that viewStates from presenter is called

            MatchDetailActivity.presenter = presenter
        }
    }

    @Test
    fun subscribe_to_presenter_view_states_and_load_details() {
        verify(presenter).loadMatchDetail(eventId)
    }

    @Test
    fun render_result_state() {
        val match = Match(
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

        renderState(MatchDetailContract.ViewState.MatchResultState(match))

        onView(withText(match.strHomeTeam)).check(matches(isDisplayed()))
        onView(withText(match.strAwayTeam)).check(matches(isDisplayed()))
    }

    private fun renderState(viewState: MatchDetailContract.ViewState) {
        rule.activity.runOnUiThread {
            rule.activity.renderState(viewState)
        }
    }

    companion object {
        private const val eventId = "441613"
    }
}