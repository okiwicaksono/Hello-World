package com.dicoding.finalsoccermatches.domain.data

import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.League
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.domain.entity.Player
import com.dicoding.finalsoccermatches.domain.entity.Team
import com.dicoding.finalsoccermatches.external.api.SoccerService
import com.dicoding.finalsoccermatches.external.database.MatchDatabase
import com.dicoding.finalsoccermatches.external.database.TeamDatabase
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async

interface SoccerRepository {
    fun getAllLeagues(): Deferred<List<League>>
    fun getPastMatches(leagueId: String): Deferred<List<Match>>
    fun getNextMatches(leagueId: String): Deferred<List<Match>>
    fun getMatchDetails(eventId: String): Deferred<Match>
    fun getMatchesByKeyword(keyword: String): Deferred<List<Match>>
    fun getTeams(leagueId: String): Deferred<List<Team>>
    fun getTeamsByKeyword(keyword: String): Deferred<List<Team>>
    fun getTeamDetails(teamId: String): Deferred<Team>
    fun getPlayers(teamId: String): Deferred<List<Player>>
    fun getPlayerDetails(playerId: String): Deferred<Player>
    fun getFavoriteMatches(context: Context): List<Match>
    fun getFavoriteTeams(context: Context): List<Team>
}

class SoccerRepositoryImpl(
    private val service: SoccerService
) : SoccerRepository {
    override fun getAllLeagues(): Deferred<List<League>> =
        GlobalScope.async {
            service.getAllLeagues().await().leagues
        }

    override fun getPastMatches(leagueId: String): Deferred<List<Match>> =
        GlobalScope.async {
            service.getPastMatches(leagueId).await().matches
        }

    override fun getNextMatches(leagueId: String): Deferred<List<Match>> =
        GlobalScope.async {
            service.getNextMatches(leagueId).await().matches
        }

    override fun getTeamDetails(teamId: String): Deferred<Team> =
        GlobalScope.async {
            service.getTeamDetails(teamId).await().teams.first()
        }

    override fun getMatchDetails(eventId: String): Deferred<Match> =
        GlobalScope.async {
            service.getMatchDetails(eventId).await().matches.first()
        }

    override fun getMatchesByKeyword(keyword: String): Deferred<List<Match>> =
        GlobalScope.async {
            service.getMatchByKeyword(keyword).await().matches
        }

    override fun getTeams(leagueId: String): Deferred<List<Team>> =
        GlobalScope.async {
            service.getTeams(leagueId).await().teams
        }

    override fun getTeamsByKeyword(keyword: String): Deferred<List<Team>> =
        GlobalScope.async {
            service.getTeamsByKeyword(keyword).await().teams
        }

    override fun getPlayers(teamId: String): Deferred<List<Player>> =
        GlobalScope.async {
            service.getPlayers(teamId).await().players
        }

    override fun getPlayerDetails(playerId: String): Deferred<Player> =
        GlobalScope.async {
            service.getPlayerDetails(playerId).await().players.first()
        }

    override fun getFavoriteMatches(context: Context): List<Match> =
        MatchDatabase(context).findAll()

    override fun getFavoriteTeams(context: Context): List<Team> =
        TeamDatabase(context).findAll()
}