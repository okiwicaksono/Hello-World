package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Team(
    @JsonProperty("idTeam")
    val idTeam: String = "",

    @JsonProperty("strTeam")
    val strTeam: String? = "",

    @JsonProperty("intFormedYear")
    val intFormedYear: String = "",

    @JsonProperty("strSport")
    val strSport: String? = "",

    @JsonProperty("strStadium")
    val strStadium: String? = "",

    @JsonProperty("strDescriptionEN")
    val strDescriptionEN: String? = "",

    @JsonProperty("strTeamBadge")
    val teamBadge: String = ""
) {
    companion object {
        const val TEAM_TABLE: String = "TEAM_TABLE"
        const val ID_TEAM: String = "idTeam"
        const val TEAM: String = "strTeam"
        const val FORMED_YEAR: String = "intFormedYear"
        const val SPORT: String = "strSport"
        const val STADIUM: String = "strStadium"
        const val DESCRIPTION: String = "strDescriptionEN"
        const val TEAM_BADGE: String = "strTeamBadge"
    }
}