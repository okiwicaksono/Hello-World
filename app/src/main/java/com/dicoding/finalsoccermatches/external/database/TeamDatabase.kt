package com.dicoding.finalsoccermatches.external.database

import android.content.Context
import com.dicoding.finalsoccermatches.domain.entity.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class TeamDatabase(private val context: Context) {
    fun findAll(): List<Team> = context.database.use {
        select(Team.TEAM_TABLE).parseList(classParser())
    }

    fun create(team: Team) = context.database.use {
        insert(
            Team.TEAM_TABLE,
            Team.ID_TEAM to team.idTeam,
            Team.TEAM to team.strTeam,
            Team.FORMED_YEAR to team.intFormedYear,
            Team.SPORT to team.strSport,
            Team.STADIUM to team.strStadium,
            Team.DESCRIPTION to team.strDescriptionEN,
            Team.TEAM_BADGE to team.teamBadge
        )
    }

    fun select(teamId: Int): Team? = context.database.use {
        select(Team.TEAM_TABLE)
            .whereArgs(
                "(idTeam = {id})",
                "id" to teamId
            ).parseOpt(classParser())
    }

    fun delete(teamId: Int) = context.database.use {
        delete(
            Team.TEAM_TABLE,
            "idTeam = {id}",
            "id" to teamId
        )
    }
}