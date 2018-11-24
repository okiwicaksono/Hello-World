package com.dicoding.finalsoccermatches.presentation.team.detail

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
import com.dicoding.finalsoccermatches.domain.entity.Player
import org.jetbrains.anko.find

class PlayerAdapter(
    private val detailListener: (Player) -> Unit
) : ListAdapter<Player, PlayerAdapter.PlayerViewHolder>(PlayerDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)

        return PlayerViewHolder(rootView).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    detailListener(getItem(position))
                }
            }
        }
    }

    override fun onBindViewHolder(viewHolder: PlayerViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.find(R.id.image)
        private val name: TextView = view.find(R.id.name)
        private val position: TextView = view.find(R.id.position)

        fun bind(player: Player) {
            Glide.with(itemView).load(player.strThumb).into(image)
            name.text = player.strPlayer
            position.text = player.strPosition
        }
    }

    class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(player: Player, other: Player): Boolean =
            player == other

        override fun areContentsTheSame(player: Player, other: Player): Boolean =
            player == other
    }
}
