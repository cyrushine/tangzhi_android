package com.ifanr.tangzhi.repository.baas

import com.ifanr.tangzhi.model.SearchKey
import io.reactivex.Single

interface SettingRepository {

    /**
     * 热门搜索
     */
    fun hottestSearch(): Single<List<SearchKey>>

    /**
     * 需隐藏的产品参数
     */
    fun hiddenProductParams(): Single<List<String>>

    /**
     * 产品参数的映射
     */
    fun productParamMapping(): Single<Map<String, String>>

    /**
     * 用户可选的 banner 图片列表
     */
    fun userBanners(): Single<List<String>>

}