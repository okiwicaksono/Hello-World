package com.dicoding.finalsoccermatches.presentation.match

import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MatchPresenter(
    private val repository: SoccerRepository
) : MatchContract.Presenter() {
    private val job = Job()
        get() {
            return if (field.isCancelled)
                Job()
            else field
        }

    private val viewStates = Channel<MatchContract.ViewState>()

    override fun viewStates(): Channel<MatchContract.ViewState> =
        viewStates

    override fun loadAllLeagues() {
        GlobalScope.launch(job) {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val leagues = repository.getAllLeagues().await()
                viewStates.send(
                    MatchContract.ViewState.LeagueResultState(leagues)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        MatchContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun loadPastMatches(leagueId: String) {
        GlobalScope.launch(job) {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getPastMatches(leagueId).await()
                viewStates.send(
                    MatchContract.ViewState.MatchResultState(matches)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        MatchContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun loadNextMatches(leagueId: String) {
        GlobalScope.launch(job) {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getNextMatches(leagueId).await()
                viewStates.send(
                    MatchContract.ViewState.MatchResultState(matches)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        MatchContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun loadMatchesByKeyword(keyword: String) {
        GlobalScope.launch(job) {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getMatchesByKeyword(keyword).await()
                viewStates.send(
                    MatchContract.ViewState.MatchResultState(matches.filter { match ->
                        match.strSport == "Soccer"
                    })
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        MatchContract.ViewState.ErrorState(
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