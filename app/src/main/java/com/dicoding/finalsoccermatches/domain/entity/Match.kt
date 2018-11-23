package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Match(
    @JsonProperty("idEvent")
    val idEvent: String = "",

    @JsonProperty("strEvent")
    val strEvent: String = "",

    @JsonProperty("strSport")
    val strSport: String = "",

    @JsonProperty("strHomeTeam")
    val strHomeTeam: String = "",

    @JsonProperty("strAwayTeam")
    val strAwayTeam: String = "",

    @JsonProperty("intHomeScore")
    val intHomeScore: Int = 0,

    @JsonProperty("intAwayScore")
    val intAwayScore: Int = 0,

    @JsonProperty("strHomeGoalDetails")
    val strHomeGoalDetails: String? = "",

    @JsonProperty("strAwayGoalDetails")
    val strAwayGoalDetails: String? = "",

    @JsonProperty("intHomeShots")
    val intHomeShots: Int = 0,

    @JsonProperty("intAwayShots")
    val intAwayShots: Int = 0,

    @JsonProperty("strHomeLineupGoalkeeper")
    val strHomeLineupGoalkeeper: String? = "",

    @JsonProperty("strHomeLineupDefense")
    val strHomeLineupDefense: String? = "",

    @JsonProperty("strHomeLineupMidfield")
    val strHomeLineupMidfield: String? = "",

    @JsonProperty("strHomeLineupForward")
    val strHomeLineupForward: String? = "",

    @JsonProperty("strHomeLineupSubstitutes")
    val strHomeLineupSubstitutes: String? = "",

    @JsonProperty("strAwayLineupGoalkeeper")
    val strAwayLineupGoalkeeper: String? = "",

    @JsonProperty("strAwayLineupDefense")
    val strAwayLineupDefense: String? = "",

    @JsonProperty("strAwayLineupMidfield")
    val strAwayLineupMidfield: String? = "",

    @JsonProperty("strAwayLineupForward")
    val strAwayLineupForward: String? = "",

    @JsonProperty("strAwayLineupSubstitutes")
    val strAwayLineupSubstitutes: String? = "",

    @JsonProperty("idHomeTeam")
    val idHomeTeam: String = "",

    @JsonProperty("idAwayTeam")
    val idAwayTeam: String = "",

    @JsonProperty("dateEvent")
    val dateEvent: String = "",

    @JsonProperty("strTime")
    val strTime: String = ""
) {
    companion object {
        const val MATCH_TABLE: String = "MATCH_TABLE"
        const val ID_EVENT: String = "idEvent"
        const val EVENT: String = "strEvent"
        const val SPORT: String = "strSport"
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
        const val EVENT_TIME: String = "strTime"
    }
}