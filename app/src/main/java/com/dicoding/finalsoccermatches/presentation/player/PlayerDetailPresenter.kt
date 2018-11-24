package com.dicoding.finalsoccermatches.presentation.player

import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class PlayerDetailPresenter(
    private val repository: SoccerRepository,
    scope: CoroutineScope
) : PlayerDetailContract.Presenter(), CoroutineScope by scope {
    private val viewStates = Channel<PlayerDetailContract.ViewState>()

    override fun viewStates(): Channel<PlayerDetailContract.ViewState> =
        viewStates

    override fun loadPlayerDetail(playerId: String) {
        launch {
            try {
                viewStates.send(PlayerDetailContract.ViewState.LoadingState)
                val player = repository.getPlayerDetails(playerId).await()
                viewStates.send(PlayerDetailContract.ViewState.PlayerResultState(player))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(PlayerDetailContract.ViewState.ErrorState(ex.message ?: ""))
            }
        }
    }
}