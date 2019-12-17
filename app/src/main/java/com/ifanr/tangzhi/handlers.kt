package com.ifanr.tangzhi

import android.os.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

private class HandlerExecutor (
    private val handler: Handler
): Executor {
    override fun execute(command: Runnable) {
        handler.post(command)
    }
}

val workerThreadHandler: Handler by lazy {
    val thread = HandlerThread("worker").apply { start() }
    Handler(thread.looper)
}

val mainThreadHandler: Handler by lazy {
    Handler(Looper.getMainLooper())
}

val mainThreadExecutor: Executor by lazy { HandlerExecutor(mainThreadHandler) }

val workerThreadExecutor: Executor by lazy { HandlerExecutor(workerThreadHandler) }