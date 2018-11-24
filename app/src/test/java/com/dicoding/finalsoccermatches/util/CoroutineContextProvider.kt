package com.dicoding.finalsoccermatches.util

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Dispatchers.Unconfined
import kotlin.coroutines.experimental.CoroutineContext

open class CoroutineContextProvider {
    open val Main: CoroutineContext by lazy { Dispatchers.Main }
    open val Default: CoroutineContext by lazy { Dispatchers.Default }
}

class TestContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Unconfined
    override val Default: CoroutineContext = Unconfined
}