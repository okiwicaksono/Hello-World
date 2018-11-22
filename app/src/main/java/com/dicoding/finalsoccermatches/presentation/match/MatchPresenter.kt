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

    override fun loadPastMatches() {
        launch {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getPastMatches().await()
                viewStates.send(
                    MatchContract.ViewState.ResultState(
                        matches
                    )
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

    override fun loadNextMatches() {
        launch {
            try {
                viewStates.send(MatchContract.ViewState.LoadingState)
                val matches = repository.getNextMatches().await()
                viewStates.send(
                    MatchContract.ViewState.ResultState(
                        matches
                    )
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
                    MatchContract.ViewState.ResultState(
                        matches
                    )
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