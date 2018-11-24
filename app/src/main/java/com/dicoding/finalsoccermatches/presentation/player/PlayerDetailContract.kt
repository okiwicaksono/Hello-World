package com.dicoding.finalsoccermatches.presentation.player

import android.arch.lifecycle.ViewModel
import com.dicoding.finalsoccermatches.domain.entity.Player
import kotlinx.coroutines.experimental.channels.Channel

interface PlayerDetailContract {
    abstract class Presenter : ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadPlayerDetail(playerId: String)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class PlayerResultState(val player: Player) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}