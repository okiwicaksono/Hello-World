package com.dicoding.finalsoccermatches.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Player(
    @JsonProperty("idPlayer")
    val idPlayer: String? = "",

    @JsonProperty("strPlayer")
    val strPlayer: String? = "",

    @JsonProperty("strDescriptionEN")
    val strDescriptionEN: String? = "",

    @JsonProperty("strPosition")
    val strPosition: String? = "",

    @JsonProperty("strHeight")
    val strHeight: String? = "",

    @JsonProperty("strWeight")
    val strWeight: String? = "",

    @JsonProperty("strThumb")
    val strThumb: String? = "",

    @JsonProperty("strFanart1")
    val strFanart1: String? = ""
)