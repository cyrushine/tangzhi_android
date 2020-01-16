package com.ifanr.tangzhi.route

object Routes {

    private const val DEFAULT_GROUP = "app"

    const val points = "/$DEFAULT_GROUP/points"

    const val updateProfile = "/$DEFAULT_GROUP/updateprofile"

    const val signIn = "/$DEFAULT_GROUP/signin"

    const val search = "/$DEFAULT_GROUP/search"

    const val index = "/$DEFAULT_GROUP/index"

    const val comment = "/$DEFAULT_GROUP/comment"
    const val commentProductId = "product_id"               // striing
    const val commentProductName = "product_name"           // string
    const val commentReviewId = "review_id"                 // string
    const val commentReviewCreatedBy = "review_created_by"  // long

    const val sendComment = "/$DEFAULT_GROUP/send_comment"
    const val sendCommentProductId = "product_id"       // string
    const val sendCommentProductName = "product_name"   // string
    const val sendCommentRootId = "root_id"             // string
    // 以下字段二级回复才需要
    const val sendCommentParentId = "parent_id"         // string, optional
    const val sendCommentReplyId = "reply_id"           // string, optional
    const val sendCommentReplyTo = "reply_to"           // long, optional

    const val sendReview = "/$DEFAULT_GROUP/send_review"
    const val sendReviewProductId = "product_id"        // string
    const val sendReviewProductName = "product_name"    // string

    const val share = "/$DEFAULT_GROUP/share"
    const val shareObject = "share_object"                    // Parcelable

    const val gallery = "/$DEFAULT_GROUP/gallery"
    const val galleryImageList = "gallery_image_list"   // string array list
    const val galleryIndex = "index"                    // int

    const val productList = "/$DEFAULT_GROUP/product_list"
    const val productListProductId = "product_id"       // id, String

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