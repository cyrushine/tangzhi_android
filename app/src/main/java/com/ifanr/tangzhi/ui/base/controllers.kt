package com.ifanr.tangzhi.ui.base

import com.airbnb.epoxy.*
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.ifanr.tangzhi.workerHandler

abstract class BaseEpoxyController: EpoxyController(
    workerHandler, workerHandler
)

abstract class BaseTypedController<T>: TypedEpoxyController<T>(
    workerHandler, workerHandler
)

abstract class BaseTyped2Controller<T, U>: Typed2EpoxyController<T, U>(
    workerHandler, workerHandler
)

abstract class BaseTyped3Controller<T, U, V>: Typed3EpoxyController<T, U, V>(
    workerHandler, workerHandler
)

abstract class BaseTyped4Controller<T, U, V, W>: Typed4EpoxyController<T, U, V, W>(
    workerHandler, workerHandler
)

abstract class BasePagedListController<T>: PagedListEpoxyController<T>(
    workerHandler, workerHandler
)