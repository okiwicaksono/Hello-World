package com.dicoding.finalsoccermatches.presentation.favorite

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.domain.entity.Team
import kotlinx.coroutines.channels.Channel

interface FavoriteContract {
    abstract class Presenter : ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadFavoriteMatches(context: Context)
        abstract fun loadFavoriteTeams(context: Context)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class MatchesResultState(val matches: List<Match>) : ViewState()
        data class TeamsResultState(val teams: List<Team>) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}