package com.ghamza.janus.core

import com.ghamza.janus.bindings.initJanusLogger

class JanusLogger {
    companion object {
        fun initialize(subsystem: String = "janus", category: String = "gateway") {
            initJanusLogger(subsystem, category)
        }
    }
}
