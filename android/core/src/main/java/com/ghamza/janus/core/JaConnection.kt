package com.ghamza.janus.core

import com.ghamza.janus.bindings.Connection
import com.ghamza.janus.bindings.janusConnect
import java.time.Duration

class JaConnection(val connection: Connection) {
    companion object {
        suspend fun connect(config: JaConfig): JaConnection {
            val connection = janusConnect(config.lower)
            return JaConnection(connection = connection)
        }
    }

    suspend fun createSession(kaInterval: UInt, timeout: Duration): JaSession {
        val session = connection.createSession(keepAliveIntervalInSecs = kaInterval, timeout = timeout)
        return JaSession(session = session)
    }
}
