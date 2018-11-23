package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class LeagueResponse(
    @JsonProperty("leagues") val leagues: List<League>
)