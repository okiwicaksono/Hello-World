package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Match(
    @JsonProperty("idEvent")
    val idEvent: String? = null,

    @JsonProperty("strHomeTeam")
    val strHomeTeam: String? = null,

    @JsonProperty("strAwayTeam")
    val strAwayTeam: String? = null,

    @JsonProperty("intHomeScore")
    val intHomeScore: String? = null,

    @JsonProperty("intAwayScore")
    val intAwayScore: String? = null,

    @JsonProperty("strHomeGoalDetails")
    val strHomeGoalDetails: String? = null,

    @JsonProperty("strAwayGoalDetails")
    val strAwayGoalDetails: String? = null,

    @JsonProperty("intHomeShots")
    val intHomeShots: String? = null,

    @JsonProperty("intAwayShots")
    val intAwayShots: String? = null,

    @JsonProperty("strHomeLineupGoalkeeper")
    val strHomeLineupGoalkeeper: String? = null,

    @JsonProperty("strHomeLineupDefense")
    val strHomeLineupDefense: String? = null,

    @JsonProperty("strHomeLineupMidfield")
    val strHomeLineupMidfield: String? = null,

    @JsonProperty("strHomeLineupForward")
    val strHomeLineupForward: String? = null,

    @JsonProperty("strHomeLineupSubstitutes")
    val strHomeLineupSubstitutes: String? = null,

    @JsonProperty("strAwayLineupGoalkeeper")
    val strAwayLineupGoalkeeper: String? = null,

    @JsonProperty("strAwayLineupDefense")
    val strAwayLineupDefense: String? = null,

    @JsonProperty("strAwayLineupMidfield")
    val strAwayLineupMidfield: String? = null,

    @JsonProperty("strAwayLineupForward")
    val strAwayLineupForward: String? = null,

    @JsonProperty("strAwayLineupSubstitutes")
    val strAwayLineupSubstitutes: String? = null,

    @JsonProperty("idHomeTeam")
    val idHomeTeam: String? = null,

    @JsonProperty("idAwayTeam")
    val idAwayTeam: String? = null,

    @JsonProperty("dateEvent")
    val dateEvent: String? = null
) {
    companion object {
        const val MATCH_TABLE: String = "MATCH_TABLE"
        const val ID_EVENT: String = "idEvent"
        const val HOME_TEAM_NAME: String = "strHomeTeam"
        const val AWAY_TEAM_NAME: String = "strAwayTeam"
        const val HOME_SCORE: String = "intHomeScore"
        const val AWAY_SCORE: String = "intAwayScore"
        const val HOME_GOAL_DETAILS: String = "strHomeGoalDetails"
        const val AWAY_GOAL_DETAILS: String = "strAwayGoalDetails"
        const val HOME_SHOTS: String = "intHomeShots"
        const val AWAY_SHOTS: String = "intAwayShots"
        const val HOME_GOALKEEPER: String = "strHomeLineupGoalkeeper"
        const val HOME_DEFENDERS: String = "strHomeLineupDefense"
        const val HOME_MIDFIELDERS: String = "strHomeLineupMidfield"
        const val HOME_STRIKERS: String = "strHomeLineupForward"
        const val HOME_SUBSTITUTES: String = "strHomeLineupSubstitutes"
        const val AWAY_GOALKEEPER: String = "strAwayLineupGoalkeeper"
        const val AWAY_DEFENDERS: String = "strAwayLineupDefense"
        const val AWAY_MIDFIELDERS: String = "strAwayLineupMidfield"
        const val AWAY_STRIKERS: String = "strAwayLineupForward"
        const val AWAY_SUBSTITUTES: String = "strAwayLineupSubstitutes"
        const val HOME_TEAM_ID: String = "idHomeTeam"
        const val AWAY_TEAM_ID: String = "idAwayTeam"
        const val EVENT_DATE: String = "dateEvent"
    }
}