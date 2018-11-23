package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class League(
    @JsonProperty("strLeague") val strLeague: String,
    @JsonProperty("idLeague") val idLeague: String
) {
    override fun toString(): String = strLeague
}