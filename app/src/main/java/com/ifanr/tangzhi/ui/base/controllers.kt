package com.ifanr.tangzhi.ui.base

import com.airbnb.epoxy.*
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.ifanr.tangzhi.mainThreadHandler
import com.ifanr.tangzhi.workerThreadHandler

abstract class BaseEpoxyController: EpoxyController(
    workerThreadHandler, workerThreadHandler
)

abstract class BaseTypedController<T>: TypedEpoxyController<T>(
    workerThreadHandler, workerThreadHandler
)

abstract class BaseTyped2Controller<T, U>: Typed2EpoxyController<T, U>(
    workerThreadHandler, workerThreadHandler
)

abstract class BaseTyped3Controller<T, U, V>: Typed3EpoxyController<T, U, V>(
    workerThreadHandler, workerThreadHandler
)

abstract class BaseTyped4Controller<T, U, V, W>: Typed4EpoxyController<T, U, V, W>(
    workerThreadHandler, workerThreadHandler
)

abstract class BasePagedListController<T>: PagedListEpoxyController<T>(
    workerThreadHandler, workerThreadHandler
)