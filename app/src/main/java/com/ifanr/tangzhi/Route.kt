package com.ifanr.tangzhi

object Routes {

    private const val DEFAULT_GROUP = "app"

    const val relatedProducts = "/$DEFAULT_GROUP/related_products"
    const val relatedProductId = "product_id"           // id, String

    const val postList = "/$DEFAULT_GROUP/post_list"
    const val postListProductId = "product_id"          // id, String

    const val product = "/$DEFAULT_GROUP/product"
    const val productId = "product_id"                  // id, String

    const val productParam = "/$DEFAULT_GROUP/product_param"
    const val productParamId = "product_id"             // product id, String

    const val browser = "/$DEFAULT_GROUP/browser"
    const val browserUrl = "url"                        // string
    const val browserTitle = "title"                    // string
}