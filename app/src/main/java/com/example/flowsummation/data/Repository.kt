package com.example.flowsummation.data

import com.example.flowsummation.domain.Interactor
import com.example.flowsummation.domain.WaysOfSum
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.yield
import javax.inject.Inject

class Repository @Inject constructor() : Interactor {

    private val array = mutableListOf<Flow<Int>>()

    override suspend fun flowSum(n: Int, waysOfSum: WaysOfSum): Flow<Int> {
        array.clear()
        return when (waysOfSum) {
            is WaysOfSum.First -> sumFirstWay(n)
            is WaysOfSum.Second -> sumSecondWay(n)
        }
    }

    @OptIn(FlowPreview::class)
    private fun sumFirstWay(n: Int): Flow<Int> {
        for (i in 0..n) {
            array.add(generateFlowFirstWay(i, n))
        }

        return flow<Int> {
            combine(flows = array, transform = { it.sum() }).debounce(10).collect { item ->
                emit(item)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
    private fun sumSecondWay(n: Int): Flow<Int> {
        for (i in 0..n) {
            array.add(generateFlowSecondWay(i, n))
        }

        return flow<Int> {
            merge(*array.toTypedArray()).bufferTimeout(size = n, durationImMillis = TIME_DELAY)
                .collect { items ->
                    emit(items.sum())
                }
        }
    }

    private fun generateFlowSecondWay(index: Int, n: Int): Flow<Int> {
        var counter = index
        var delay = (index + 1) + TIME_DELAY
        return flow<Int> {
            while (counter < n) {
                delay(delay)
                emit(index + 1)
                counter++
                delay = TIME_DELAY
                yield()
            }
        }
    }

    private fun generateFlowFirstWay(index: Int, n: Int): Flow<Int> {
        var counter = 0
        return flow<Int> {
            while (counter < n) {
                delay(TIME_DELAY)
                if (counter < index) {
                    emit(0)
                } else if (counter <= n) {
                    emit(index + 1)
                }
                counter++
                yield()
            }
        }
    }

    /***
     * взято из https://dev.to/psfeng/a-story-of-building-a-custom-flow-operator-buffertimeout-4d95
     */
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    fun <T> Flow<T>.bufferTimeout(size: Int, durationImMillis: Long): Flow<List<T>> {
        require(size > 0) { "Window size should be greater than 0" }
        require(durationImMillis > 0) { "Duration should be greater than 0" }

        return flow {
            coroutineScope {
                val events = ArrayList<T>(size)
                val tickerChannel = ticker(durationImMillis)
                try {
                    val upstreamValues = produce { collect { send(it) } }

                    while (isActive) {
                        var hasTimedOut = false

                        select<Unit> {
                            upstreamValues.onReceive {
                                events.add(it)
                            }

                            tickerChannel.onReceive {
                                hasTimedOut = true
                            }
                        }

                        if (events.size == size || (hasTimedOut && events.isNotEmpty())) {
                            emit(events.toList())
                            events.clear()
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                    if (events.isNotEmpty()) emit(events.toList())
                } finally {
                    tickerChannel.cancel()
                }
            }
        }
    }

    companion object {
        const val TIME_DELAY = 100L
    }
}