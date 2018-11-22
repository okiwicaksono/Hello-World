package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Team(
    @JsonProperty("strTeamBadge") val teamBadge: String
)