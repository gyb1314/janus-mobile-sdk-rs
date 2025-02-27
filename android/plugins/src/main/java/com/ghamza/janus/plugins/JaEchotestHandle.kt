package com.ghamza.janus.plugins

import com.ghamza.janus.bindings.EchotestHandle
import com.ghamza.janus.bindings.EchotestHandleCallback
import com.ghamza.janus.bindings.EchoTestStartParams
import com.ghamza.janus.bindings.GenericEvent
import com.ghamza.janus.bindings.Jsep
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.time.Duration

class JaEchotestHandle(val handle: EchotestHandle): EchotestHandleCallback {
    private var events: ProducerScope<JaEchotestEvent>? = null

    fun stream() = callbackFlow {
        events = this
        handle.startEventLoop(this@JaEchotestHandle)
        awaitClose { }
    }

    suspend fun start(audio: Boolean = false, video: Boolean = false, bitrate: UInt? = null) {
        val params = EchoTestStartParams(
            audio = audio,
            video = video,
            bitrate = bitrate
        )
        handle.start(params = params)
    }

    suspend fun start(
        audio: Boolean = false,
        video: Boolean = false,
        bitrate: UInt? = null,
        jsep: Jsep,
        timeout: Duration
    ) {
        val params = EchoTestStartParams(
            audio = audio,
            video = video,
            bitrate = bitrate
        )
        handle.startWithJsep(params = params, jsep = jsep, timeout = timeout)
    }

    override fun onResult(echotest: String, result: String) {
        events?.trySend(JaEchotestEvent.Result(echotest = echotest, result = result))
    }

    override fun onResultWithJsep(echotest: String, result: String, jsep: Jsep) {
        events?.trySend(JaEchotestEvent.ResultWithJsep(echotest = echotest, result = result, jsep = jsep))
    }
    
    override fun onEchoTestError(errorCode: UShort, error: String) {
        events?.trySend(JaEchotestEvent.Error(errorCode = errorCode, error = error))
    }
    
    override fun onHandleEvent(event: GenericEvent) {
     
    }
    
    override fun onOther(data: ByteArray) {

    }
}

sealed interface JaEchotestEvent {
    data class Result(val echotest: String, val result: String): JaEchotestEvent
    data class ResultWithJsep(val echotest: String, val result: String, val jsep: Jsep): JaEchotestEvent
    data class Error(val errorCode: UShort, val error: String): JaEchotestEvent
}
