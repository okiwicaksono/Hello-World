package com.dicoding.finalsoccermatches.presentation.team

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dicoding.finalsoccermatches.R
import com.dicoding.finalsoccermatches.domain.entity.Team
import org.jetbrains.anko.find

class TeamAdapter(
    private val detailListener: (Team) -> Unit
) : ListAdapter<Team, TeamAdapter.TeamViewHolder>(TeamDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team, parent, false)

        return TeamViewHolder(rootView).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    detailListener(getItem(position))
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: TeamViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.find(R.id.imageView)
        private val textView: TextView = view.find(R.id.textView)

        fun bind(team: Team) {
            Glide.with(itemView).load(team.teamBadge).into(imageView)
            textView.text = team.strTeam
        }
    }

    class TeamDiffCallback : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(team: Team, other: Team): Boolean =
            team == other

        override fun areContentsTheSame(team: Team, other: Team): Boolean =
            team == other
    }
}
