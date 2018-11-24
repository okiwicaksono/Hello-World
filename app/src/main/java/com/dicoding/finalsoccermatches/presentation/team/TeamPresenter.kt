package com.dicoding.finalsoccermatches.presentation.team

import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

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
                val teams = repository.getTeams(leagueId).await()
                viewStates.send(
                    TeamContract.ViewState.TeamsResultState(teams)
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
                    TeamContract.ViewState.TeamsResultState(teams.filter { team ->
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

    override fun loadPlayers(teamId: String) {
        launch {
            try {
                viewStates.send(TeamContract.ViewState.LoadingState)
                val teams = repository.getPlayers(teamId).await()
                viewStates.send(
                    TeamContract.ViewState.PlayersResultState(teams)
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

    override fun loadTeamDetails(teamId: String) {
        launch {
            try {
                viewStates.send(TeamContract.ViewState.LoadingState)
                val team = repository.getTeamDetails(teamId).await()
                viewStates.send(
                    TeamContract.ViewState.TeamDetailsResultState(team)
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