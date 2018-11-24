package com.dicoding.finalsoccermatches.presentation.match.detail

import android.arch.lifecycle.ViewModel
import android.widget.ImageView
import com.dicoding.finalsoccermatches.domain.entity.Match
import kotlinx.coroutines.channels.Channel

interface MatchDetailContract {
    abstract class Presenter : ViewModel() {
        abstract fun viewStates(): Channel<ViewState>
        abstract fun loadTeamBadge(teamId: String, imageView: ImageView)
        abstract fun loadMatchDetail(eventId: String)
    }

    interface View {
        fun renderState(viewState: ViewState)
    }

    sealed class ViewState {
        object LoadingState : ViewState()
        data class URLResultState(val imageUrl: String, val imageView: ImageView) : ViewState()
        data class MatchResultState(val match: Match) : ViewState()
        class ErrorState(val error: String) : ViewState()
    }
}