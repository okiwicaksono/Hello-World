package com.dicoding.finalsoccermatches.presentation.team

import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class TeamPresenter(
    private val repository: SoccerRepository,
    scope: CoroutineScope
) : TeamContract.Presenter(), CoroutineScope by scope {
    private val viewStates = Channel<TeamContract.ViewState>()

    override fun viewStates(): Channel<TeamContract.ViewState> =
        viewStates

    override fun loadAllLeagues() {
        launch {
            try {
                viewStates.send(TeamContract.ViewState.LoadingState)
                val leagues = repository.getAllLeagues().await()
                viewStates.send(
                    TeamContract.ViewState.LeagueResultState(leagues)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        TeamContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun loadTeams(leagueId: String) {
        launch {
            try {
                viewStates.send(TeamContract.ViewState.LoadingState)
                val teams = repository.getTeam(leagueId).await()
                viewStates.send(
                    TeamContract.ViewState.TeamResultState(teams)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        TeamContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun loadTeamsByKeyword(keyword: String) {
        launch {
            try {
                viewStates.send(TeamContract.ViewState.LoadingState)
                val teams = repository.getTeamsByKeyword(keyword).await()
                viewStates.send(
                    TeamContract.ViewState.TeamResultState(teams.filter { team ->
                        team.strSport == "Soccer"
                    })
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        TeamContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun onCleared() {
        viewStates.cancel()
    }
}