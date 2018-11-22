package com.dicoding.finalsoccermatches.domain.data

import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.domain.entity.Team
import com.dicoding.finalsoccermatches.external.api.MatchService
import com.dicoding.finalsoccermatches.external.database.MatchDatabase
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async

interface MatchRepository {
    fun getPastMatches(): Deferred<List<Match>>
    fun getNextMatches(): Deferred<List<Match>>
    fun getFavoriteMatches(context: Context): List<Match>
    fun getTeamBadge(teamId: String): Deferred<Team>
    fun getMatchDetails(eventId: String): Deferred<Match>
}

class MatchRepositoryImpl(
    private val service: MatchService
) : MatchRepository {
    override fun getPastMatches(): Deferred<List<Match>> =
        GlobalScope.async {
            service.getPastMatches().await().matches
        }

    override fun getNextMatches(): Deferred<List<Match>> =
        GlobalScope.async {
            service.getNextMatches().await().matches
        }

    override fun getFavoriteMatches(context: Context): List<Match> =
        MatchDatabase(context).findAll()

    override fun getTeamBadge(teamId: String): Deferred<Team> =
        GlobalScope.async {
            service.getTeamBadge(teamId).await().teams.first()
        }

    override fun getMatchDetails(eventId: String): Deferred<Match> =
        GlobalScope.async {
            service.getMatchDetails(eventId).await().matches.first()
        }
}