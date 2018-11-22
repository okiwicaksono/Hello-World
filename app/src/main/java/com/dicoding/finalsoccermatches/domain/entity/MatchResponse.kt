package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchResponse(
    @JsonProperty("events") val matches: List<Match>
)