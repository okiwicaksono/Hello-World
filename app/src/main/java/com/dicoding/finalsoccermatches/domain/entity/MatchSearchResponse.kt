package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchSearchResponse(
    @JsonProperty("event") val matches: List<Match>
)