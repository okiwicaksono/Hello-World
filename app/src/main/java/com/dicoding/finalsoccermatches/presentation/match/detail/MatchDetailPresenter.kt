package com.dicoding.finalsoccermatches.presentation.match.detail

import android.widget.ImageView
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MatchDetailPresenter(
    private val repository: SoccerRepository,
    scope: CoroutineScope
) : MatchDetailContract.Presenter(), CoroutineScope by scope {
    private val viewStates = Channel<MatchDetailContract.ViewState>()

    override fun viewStates(): Channel<MatchDetailContract.ViewState> =
        viewStates

    override fun loadTeamBadge(teamId: String, imageView: ImageView) {
        launch {
            try {
                viewStates.send(MatchDetailContract.ViewState.LoadingState)
                val team = repository.getTeamDetails(teamId).await()
                viewStates.send(MatchDetailContract.ViewState.URLResultState(team.teamBadge, imageView))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(MatchDetailContract.ViewState.ErrorState(ex.message ?: ""))
            }
        }
    }

    override fun loadMatchDetail(eventId: String) {
        launch {
            try {
                viewStates.send(MatchDetailContract.ViewState.LoadingState)
                val match = repository.getMatchDetails(eventId).await()
                viewStates.send(MatchDetailContract.ViewState.MatchResultState(match))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(MatchDetailContract.ViewState.ErrorState(ex.message ?: ""))
            }
        }
    }
}