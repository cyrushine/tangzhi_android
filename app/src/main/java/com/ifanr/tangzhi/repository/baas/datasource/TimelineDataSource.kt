package com.ifanr.tangzhi.repository.baas.datasource

import com.ifanr.tangzhi.model.Timeline
import com.ifanr.tangzhi.repository.baas.BaasRepository
import com.ifanr.tangzhi.repository.baas.timelineTable
import com.minapp.android.sdk.database.Record
import com.minapp.android.sdk.database.query.Where

/**
 * 我的动态列表
 */
class TimelineDataSource(
    private val repository: BaasRepository
): BaseDataSource<Timeline>(
    table = timelineTable,
    clz = Timeline::class.java,
    initQuery = {
        put(Where().apply {
            equalTo(Record.CREATED_BY, repository.currentUserWithoutData()?.id)
            equalTo(Timeline.COL_TYPE, Timeline.TYPE_PRODUCT)
        })
        orderBy("-${Record.CREATED_BY}")
    }
)