package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerDetailResponse(
    @JsonProperty("players") val players: List<Player>
)