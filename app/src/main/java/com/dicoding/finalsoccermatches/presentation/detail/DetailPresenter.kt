package com.dicoding.finalsoccermatches.presentation.detail

import android.widget.ImageView
import com.dicoding.finalsoccermatches.domain.data.MatchRepository
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class DetailPresenter(
    private val repository: MatchRepository
) : DetailContract.Presenter() {
    private val compositeJob = Job()
        get() {
            return if (field.isCancelled)
                Job() // canceled job can not be reused, so create a new
            else field
        }

    private val viewStates = Channel<DetailContract.ViewState>()

    override fun viewStates(): Channel<DetailContract.ViewState> =
        viewStates

    override fun loadTeamBadge(teamId: String, imageView: ImageView) {
        GlobalScope.launch(compositeJob) {
            try {
                viewStates.send(DetailContract.ViewState.LoadingState)
                val team = repository.getTeamBadge(teamId).await()
                viewStates.send(DetailContract.ViewState.URLResultState(team.teamBadge, imageView))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(DetailContract.ViewState.ErrorState(ex.message ?: ""))
            }
        }
    }

    override fun loadMatchDetail(eventId: String) {
        GlobalScope.launch(compositeJob) {
            try {
                viewStates.send(DetailContract.ViewState.LoadingState)
                val match = repository.getMatchDetails(eventId).await()
                viewStates.send(DetailContract.ViewState.MatchResultState(match))
            } catch (ex: Exception) {
                if (ex !is CancellationException)
                    viewStates.send(DetailContract.ViewState.ErrorState(ex.message ?: ""))
            }
        }
    }
}