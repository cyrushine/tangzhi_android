package com.ifanr.tangzhi.ui.updateprofile

import androidx.lifecycle.MutableLiveData
import com.ifanr.tangzhi.Event
import com.ifanr.tangzhi.EventBus
import com.ifanr.tangzhi.exceptions.NeedSignInException
import com.ifanr.tangzhi.model.UserProfile
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.repository.baas.SettingRepository
import com.ifanr.tangzhi.ui.base.BaseViewModel
import com.ifanr.tangzhi.ui.base.autoDispose
import com.minapp.android.sdk.auth.Auth
import io.reactivex.Completable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class UpdateProfileViewModel @Inject constructor(
    private val repository: BaasRepository,
    private val bus: EventBus,
    private val settingRepository: SettingRepository
): BaseViewModel() {

    val currentProfile = MutableLiveData<UserProfile>()

    /**
     * 头像的显示内容依赖它
     * 背景图片的选中依赖它
     * 其他字段只作为 ui 的收集功能
     */
    val profileForm =
        MutableLiveData<UserProfile>().apply { value = UserProfile() }
    val loading = MutableLiveData<Boolean>()
    val toast = MutableLiveData<String>()
    val banners = MutableLiveData<List<String>>()

    init {

        currentProfile.observeForever { it?.also {
            profileForm.value = profileForm.value?.copy(
                banner = it.banner,
                displayAvatar = it.displayAvatar)
        }}

        // 加载 banners
        settingRepository.userBanners()
            .subscribeOn(Schedulers.io())
            .autoDispose(this)
            .subscribe({
                banners.postValue(it)
            }, {
                toast.postValue(it?.message)
            })

        // 加载个人资料
        repository.loadUserProfile()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { loading.postValue(true) }
            .doAfterTerminate { loading.postValue(false) }
            .autoDispose(this)
            .subscribe({
                currentProfile.postValue(it)
            }, {
                toast.postValue(it?.message)
            })

        bus.subscribe(vm = this, onNext = Consumer {
            when (it) {
                is Event.UserPhoneChanged -> {
                    currentProfile.value = currentProfile.value?.copy(phone = it.phone)
                }
            }
        })
    }

    /**
     * 选中某个 banner
     */
    fun onBannerSelected(position: Int) {
        banners.value?.getOrNull(position)?.also {
            profileForm.value = profileForm.value?.copy(banner = it)
        }
    }

    /**
     * 更新个人资料
     */
    fun save() = Completable.fromCallable {
        loading.postValue(true)
        try {
            val profile = currentProfile.value ?: throw NeedSignInException()
            var update = profileForm.value?.copy() ?: throw IllegalStateException()

            // 如果有更新头像，此时的头像地址应该是 file path
            if (profile.displayAvatar != update.displayAvatar) {
                val file = File(update.displayAvatar)
                val avatarPath =
                    repository.uploadUserAvatar(file.name, file.readBytes()).blockingGet().path
                        ?: throw IllegalStateException("${update.displayAvatar} 上传失败")
                update = update.copy(displayAvatar = avatarPath)
            }

            // 更新个人资料
            repository.updateProfile(update).blockingAwait()
        } finally {
            loading.postValue(false)
        }
    }
}