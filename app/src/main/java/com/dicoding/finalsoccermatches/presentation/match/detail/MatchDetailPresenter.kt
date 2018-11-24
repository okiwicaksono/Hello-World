package com.dicoding.finalsoccermatches.presentation.match.detail

import android.widget.ImageView
import com.dicoding.finalsoccermatches.domain.data.SoccerRepository
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch

class MatchDetailPresenter(
    private val repository: SoccerRepository
) : MatchDetailContract.Presenter() {
    private val compositeJob = Job()
        get() {
            return if (field.isCancelled)
                Job() // canceled job can not be reused, so create a new
            else field
        }

    private val viewStates = Channel<MatchDetailContract.ViewState>()

    override fun viewStates(): Channel<MatchDetailContract.ViewState> =
        viewStates

    override fun loadTeamBadge(teamId: String, imageView: ImageView) {
        GlobalScope.launch(compositeJob) {
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
        GlobalScope.launch(compositeJob) {
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