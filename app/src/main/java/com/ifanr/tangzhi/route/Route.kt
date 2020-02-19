package com.ifanr.tangzhi.route

object Routes {

    private const val DEFAULT_GROUP = "app"

    const val points = "/$DEFAULT_GROUP/points"

    const val updateProfile = "/$DEFAULT_GROUP/updateprofile"

    const val signInByWechat = "/$DEFAULT_GROUP/signin_by_wechat"

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
    const val sendReviewScore = "score"                 // float, [0.0, 10.0]，产品的打分, optional


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
    const val browserUrl = "url"                            // string
    const val browserTitle = "title"                        // string
    // Boolean，== true && title != null 时，标题固定不变
    const val browserTitleNotChanged = "title_not_changed"

    const val signInByPhone = "/$DEFAULT_GROUP/sign_in_by_phone"
    const val signInByPhoneNumber = "phone_number"      // String，手机号码

    const val signInByEmail = "/$DEFAULT_GROUP/sign_in_by_email"
    const val bindLocalPhone = "/$DEFAULT_GROUP/bind_local_phone"

    const val bindPhone = "/$DEFAULT_GROUP/bind_phone"
    const val bindPhoneType = "type"                    // String，页面样式（wechat or email）

    const val message = "/$DEFAULT_GROUP/message"       // 我的消息
    const val follows = "/$DEFAULT_GROUP/follows"       // 我的关注
    const val timeline = "/$DEFAULT_GROUP/timeline"     // 我的动态
}