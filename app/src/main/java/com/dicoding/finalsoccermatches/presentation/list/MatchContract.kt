package com.dicoding.finalsoccermatches.presentation.list

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.Match
import kotlinx.coroutines.experimental.channels.Channel

interface MatchContract {
    abstract class Presenter: ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadPastMatches()
        abstract fun loadNextMatches()
        abstract fun loadFavoriteMatches(context: Context)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState: ViewState()
        data class ResultState(val matches: List<Match>): ViewState()
        class ErrorState(val error: String): ViewState()
    }
}