package com.dicoding.finalsoccermatches.presentation.team

import android.arch.lifecycle.ViewModel
import com.dicoding.finalsoccermatches.domain.entity.League
import com.dicoding.finalsoccermatches.domain.entity.Team
import kotlinx.coroutines.experimental.channels.Channel

interface TeamContract {
    abstract class Presenter : ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadAllLeagues()
        abstract fun loadTeams(leagueId: String)
        abstract fun loadTeamsByKeyword(keyword: String)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class LeagueResultState(val leagues: List<League>) : ViewState()
        data class TeamResultState(val teams: List<Team>) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}