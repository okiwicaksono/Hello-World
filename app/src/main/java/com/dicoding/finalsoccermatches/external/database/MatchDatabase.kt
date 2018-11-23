package com.dicoding.finalsoccermatches.external.database

import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.Match
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class MatchDatabase(private val context: Context) {
    fun findAll(): List<Match> = context.database.use {
        select(Match.MATCH_TABLE).parseList(classParser())
    }

    fun create(match: Match) = context.database.use {
        insert(
            Match.MATCH_TABLE,
            Match.ID_EVENT to match.idEvent,
            Match.HOME_TEAM_NAME to match.strHomeTeam,
            Match.AWAY_TEAM_NAME to match.strAwayTeam,
            Match.HOME_SCORE to match.intHomeScore,
            Match.AWAY_SCORE to match.intAwayScore,
            Match.HOME_GOAL_DETAILS to match.strHomeGoalDetails,
            Match.AWAY_GOAL_DETAILS to match.strAwayGoalDetails,
            Match.HOME_SHOTS to match.intHomeShots,
            Match.AWAY_SHOTS to match.intAwayShots,
            Match.HOME_GOALKEEPER to match.strHomeLineupGoalkeeper,
            Match.HOME_DEFENDERS to match.strHomeLineupDefense,
            Match.HOME_MIDFIELDERS to match.strHomeLineupMidfield,
            Match.HOME_STRIKERS to match.strHomeLineupForward,
            Match.HOME_SUBSTITUTES to match.strHomeLineupSubstitutes,
            Match.AWAY_GOALKEEPER to match.strAwayLineupGoalkeeper,
            Match.AWAY_DEFENDERS to match.strAwayLineupDefense,
            Match.AWAY_MIDFIELDERS to match.strAwayLineupMidfield,
            Match.AWAY_STRIKERS to match.strAwayLineupForward,
            Match.AWAY_SUBSTITUTES to match.strAwayLineupSubstitutes,
            Match.HOME_TEAM_ID to match.idHomeTeam,
            Match.AWAY_TEAM_ID to match.idAwayTeam,
            Match.EVENT_DATE to match.dateEvent,
            Match.EVENT_TIME to match.strTime
        )
    }

    fun select(matchId: Int): Match? = context.database.use {
        select(Match.MATCH_TABLE)
            .whereArgs(
                "(idEvent = {id})",
                "id" to matchId
            ).parseOpt(classParser())
    }

    fun delete(matchId: Int) = context.database.use {
        delete(
            Match.MATCH_TABLE,
            "idEvent = {id}",
            "id" to matchId
        )
    }
}