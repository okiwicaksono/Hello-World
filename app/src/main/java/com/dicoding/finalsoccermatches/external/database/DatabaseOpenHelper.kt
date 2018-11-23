package com.dicoding.finalsoccermatches.external.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.dicoding.finalsoccermatches.domain.entity.Match
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FavoriteMatches.db", null, 1) {
    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as DatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            Match.MATCH_TABLE, true,
            Match.ID_EVENT to TEXT + PRIMARY_KEY,
            Match.HOME_TEAM_NAME to TEXT,
            Match.AWAY_TEAM_NAME to TEXT,
            Match.HOME_SCORE to TEXT,
            Match.AWAY_SCORE to TEXT,
            Match.HOME_GOAL_DETAILS to TEXT,
            Match.AWAY_GOAL_DETAILS to TEXT,
            Match.HOME_SHOTS to TEXT,
            Match.AWAY_SHOTS to TEXT,
            Match.HOME_GOALKEEPER to TEXT,
            Match.HOME_DEFENDERS to TEXT,
            Match.HOME_MIDFIELDERS to TEXT,
            Match.HOME_STRIKERS to TEXT,
            Match.HOME_SUBSTITUTES to TEXT,
            Match.AWAY_GOALKEEPER to TEXT,
            Match.AWAY_DEFENDERS to TEXT,
            Match.AWAY_MIDFIELDERS to TEXT,
            Match.AWAY_STRIKERS to TEXT,
            Match.AWAY_SUBSTITUTES to TEXT,
            Match.HOME_TEAM_ID to TEXT,
            Match.AWAY_TEAM_ID to TEXT,
            Match.EVENT_DATE to TEXT,
            Match.EVENT_TIME to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(Match.MATCH_TABLE, true)
    }
}

val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)