package com.ifanr.tangzhi.ui.base

import com.airbnb.epoxy.*

abstract class BaseEpoxyController: EpoxyController()

abstract class BaseTypedController<T>: TypedEpoxyController<T>()

abstract class BaseTyped2Controller<T, U>: Typed2EpoxyController<T, U>()

abstract class BaseTyped3Controller<T, U, V>: Typed3EpoxyController<T, U, V>()

abstract class BaseTyped4Controller<T, U, V, W>: Typed4EpoxyController<T, U, V, W>()