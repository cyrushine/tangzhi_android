package com.ifanr.tangzhi

import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class TestViewModel (
    var age: Int
): BaseViewModel() {

    fun growing(delay: Long, value: Int) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
            .autoDispose(this)
            .subscribe { age = value }
    }

    fun invokeOnCleared() {
        onCleared()
    }
}

class BaseViewModelTest: BaseTest() {

    @Test
    fun test1() {
        val init = 30
        val value = 50
        val viewModel = TestViewModel(age = 30)
        assertEquals(init, viewModel.age)

        viewModel.growing(delay = 5000, value = value)
        Thread.sleep(3000)
        assertEquals(init, viewModel.age)

        Thread.sleep(3000)
        assertEquals(value, viewModel.age)
    }

    @Test
    fun test2() {
        val init = 30
        val value = 50
        val viewModel = TestViewModel(age = 30)
        assertEquals(init, viewModel.age)

        viewModel.growing(delay = 5000, value = value)
        Thread.sleep(3000)
        assertEquals(init, viewModel.age)
        viewModel.invokeOnCleared()

        Thread.sleep(3000)
        assertEquals(init, viewModel.age)
    }

}