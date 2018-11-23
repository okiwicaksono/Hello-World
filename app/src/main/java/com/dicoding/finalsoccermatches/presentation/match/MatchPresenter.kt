package com.dicoding.finalsoccermatches.presentation.match

import android.content.Context
import com.dicoding.finalsoccermatches.domain.data.MatchRepository
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class MatchPresenter(
    private val repository: MatchRepository,
    scope: CoroutineScope
) : MatchContract.Presenter(), CoroutineScope by scope {
    private val viewStates = Channel<MatchContract.ViewState>()

    override fun viewStates(): Channel<MatchContract.ViewState> =
        viewStates

    override fun loadAllLeagues() {
        launch {
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
        launch {
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
        launch {
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

    override fun loadFavoriteMatches(context: Context) {
        launch {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getFavoriteMatches(context)
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
        launch {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getMatchByKeyword(keyword).await()
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