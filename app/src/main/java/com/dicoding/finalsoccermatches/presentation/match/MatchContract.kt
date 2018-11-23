package com.dicoding.finalsoccermatches.presentation.match

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.League
import com.dicoding.finalsoccermatches.domain.entity.Match
import kotlinx.coroutines.experimental.channels.Channel

interface MatchContract {
    abstract class Presenter : ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadAllLeagues()
        abstract fun loadPastMatches(leagueId: String)
        abstract fun loadNextMatches(leagueId: String)
        abstract fun loadFavoriteMatches(context: Context)
        abstract fun loadMatchesByKeyword(keyword: String)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class LeagueResultState(val leagues: List<League>) : ViewState()
        data class MatchResultState(val matches: List<Match>) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}