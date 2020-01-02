package com.ifanr.tangzhi

import android.os.*
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.*
import kotlin.math.max

private const val WORKER_MAX = 4

val workers: ScheduledExecutorService by lazy {
    Executors.newScheduledThreadPool(max(Runtime.getRuntime().availableProcessors(), WORKER_MAX))
}

val workerScheduler: Scheduler by lazy {
    Schedulers.from(workers)
}

val workerSchedulerCallable = Callable { workerScheduler }

val workerHandler: Handler by lazy {
    val thread = HandlerThread("worker").apply { start() }
    Handler(thread.looper)
}

val worker: Executor by lazy { HandlerExecutor(workerHandler) }

val mainHandler: Handler by lazy {
    Handler(Looper.getMainLooper())
}


class HandlerExecutor (
    private val handler: Handler
): Executor {
    override fun execute(command: Runnable) {
        handler.post(command)
    }
}