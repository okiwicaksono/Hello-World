package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Team(
    @JsonProperty("idTeam")
    val idTeam: String? = "",

    @JsonProperty("strTeam")
    val strTeam: String? = "",

    @JsonProperty("intFormedYear")
    val intFormedYear: Int = 0,

    @JsonProperty("strSport")
    val strSport: String? = "",

    @JsonProperty("strStadium")
    val strStadium: String? = "",

    @JsonProperty("strDescriptionEN")
    val strDescriptionEN: String? = "",

    @JsonProperty("strTeamBadge")
    val teamBadge: String = ""
)