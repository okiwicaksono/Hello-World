package com.dicoding.finalsoccermatches.presentation.match

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.entity.Match
import com.dicoding.finalsoccermatches.parseToDesiredDate
import com.dicoding.finalsoccermatches.parseToDesiredTime
import com.dicoding.finalsoccermatches.visible
import org.jetbrains.anko.find

class MatchAdapter(private val listener: (Match) -> Unit) :
    ListAdapter<Match, MatchAdapter.MatchViewHolder>(MatchDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)

        return MatchViewHolder(rootView).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(getItem(position))
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: MatchViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.find(R.id.date)
        private val time: TextView = view.find(R.id.time)
        private val homeScore: TextView = view.find(R.id.homeScore)
        private val awayScore: TextView = view.find(R.id.awayScore)
        private val homeTeamName: TextView = view.find(R.id.homeTeamName)
        private val awayTeamName: TextView = view.find(R.id.awayTeamName)

        fun bind(match: Match) {
            match.dateEvent?.let { dateEvent ->
                match.strTime?.let { strTime ->
                    date.text = parseToDesiredDate(dateString = dateEvent, timeString = strTime)
                    time.text = parseToDesiredTime(dateString = dateEvent, timeString = strTime)
                }
            }
            match.intHomeScore?.let {
                homeScore.text = match.intHomeScore
                homeScore.visible()
            }
            match.intAwayScore?.let {
                awayScore.text = match.intAwayScore
                awayScore.visible()
            }
            homeTeamName.text = match.strHomeTeam
            awayTeamName.text = match.strAwayTeam
        }
    }

    class MatchDiffCallback : DiffUtil.ItemCallback<Match>() {
        override fun areItemsTheSame(match: Match, other: Match): Boolean =
            match == other

        override fun areContentsTheSame(match: Match, other: Match): Boolean =
            match == other
    }
}
