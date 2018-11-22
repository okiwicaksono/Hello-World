package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class TeamResponse(
    @JsonProperty("teams") val teams: List<Team>
)