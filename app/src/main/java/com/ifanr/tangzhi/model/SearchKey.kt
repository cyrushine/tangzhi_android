package com.ifanr.tangzhi.model

import com.google.gson.annotations.SerializedName

class SearchKey {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("keywords")
    var keywords: List<String>? = null
}