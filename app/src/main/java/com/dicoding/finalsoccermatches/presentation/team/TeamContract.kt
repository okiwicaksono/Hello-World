package com.dicoding.finalsoccermatches.presentation.team

import android.arch.lifecycle.ViewModel
import com.dicoding.finalsoccermatches.domain.entity.League
import com.dicoding.finalsoccermatches.domain.entity.Player
import com.dicoding.finalsoccermatches.domain.entity.Team
import kotlinx.coroutines.channels.Channel

interface TeamContract {
    abstract class Presenter : ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadAllLeagues()
        abstract fun loadTeams(leagueId: String)
        abstract fun loadTeamsByKeyword(keyword: String)
        abstract fun loadTeamDetails(teamId: String)
        abstract fun loadPlayers(teamId: String)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class LeagueResultState(val leagues: List<League>) : ViewState()
        data class TeamsResultState(val teams: List<Team>) : ViewState()
        data class TeamDetailsResultState(val team: Team) : ViewState()
        data class PlayersResultState(val players: List<Player>) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}