package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerResponse(
    @JsonProperty("player") val players: List<Player>
)