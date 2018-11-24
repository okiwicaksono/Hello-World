package com.dicoding.finalsoccermatches.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider {
    open val Main: CoroutineContext by lazy { Dispatchers.Main }
    open val Default: CoroutineContext by lazy { Dispatchers.Default }
}

@ExperimentalCoroutinesApi
class TestContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Unconfined
    override val Default: CoroutineContext = Unconfined
}