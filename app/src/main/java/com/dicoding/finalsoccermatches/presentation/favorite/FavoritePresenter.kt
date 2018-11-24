package com.dicoding.finalsoccermatches.presentation.favorite

import android.content.Context
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class FavoritePresenter(
    private val repository: SoccerRepository,
    scope: CoroutineScope
) : FavoriteContract.Presenter(), CoroutineScope by scope {
    private val viewStates = Channel<FavoriteContract.ViewState>()

    override fun viewStates(): Channel<FavoriteContract.ViewState> =
        viewStates

    override fun loadFavoriteMatches(context: Context) {
        launch {
            try {
                viewStates.send(FavoriteContract.ViewState.LoadingState)
                val matches = repository.getFavoriteMatches(context)
                viewStates.send(
                    FavoriteContract.ViewState.MatchesResultState(matches)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        FavoriteContract.ViewState.ErrorState(
                            ex.message ?: ""
                        )
                    )
            }
        }
    }

    override fun loadFavoriteTeams(context: Context) {
        launch {
            try {
                viewStates.send(FavoriteContract.ViewState.LoadingState)
                val teams = repository.getFavoriteTeams(context)
                viewStates.send(
                    FavoriteContract.ViewState.TeamsResultState(teams)
                )
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(
                        FavoriteContract.ViewState.ErrorState(
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