package com.dicoding.finalsoccermatches.external.api

import com.dicoding.finalsoccermatches.domain.entity.MatchResponse
import com.dicoding.finalsoccermatches.domain.entity.TeamResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface MatchService {
    @GET ("api/v1/json/1/eventspastleague.php?id=4328")
    fun getPastMatches(): Deferred<MatchResponse>

    @GET ("api/v1/json/1/eventsnextleague.php?id=4328")
    fun getNextMatches(): Deferred<MatchResponse>

    @GET ("api/v1/json/1/lookupteam.php?id={id}")
    fun getTeamBadge(@Path("id") id: String): Deferred<TeamResponse>

    @GET ("api/v1/json/1/lookupevent.php?id={id}")
    fun getMatchDetails(@Path("id") id: String): Deferred<MatchResponse>
}