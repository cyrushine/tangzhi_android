package com.ifanr.android.tangzhi.ui.product.indexes

import com.ifanr.android.tangzhi.model.Product
import org.parceler.Parcel
import org.parceler.ParcelConstructor

@Parcel(Parcel.Serialization.BEAN)
data class ProductRating @ParcelConstructor constructor(
    val name: String,
    val rating: Float,
    val usersRating: UsersRating,
    val orgRating: List<RatingRecord>,
    val thirdPartyRating: List<RatingRecord>
) {

    constructor(product: Product): this(
        name = product.name,
        rating = product.rating,
        usersRating = UsersRating (count = product.reviewCount, rating = product.userRating),
        orgRating = product.orgRating.map { RatingRecord (name = it.name, rating = it.rating) },
        thirdPartyRating = product.thirdPartyRating.map { RatingRecord(name = it.name, rating = it.rating) }
    )

    @Parcel(Parcel.Serialization.BEAN)
    data class UsersRating @ParcelConstructor constructor (
        val count: Long,
        val rating: Float
    )

    @Parcel(Parcel.Serialization.BEAN)
    data class RatingRecord @ParcelConstructor constructor (
        val name: String,
        val rating: Float
    )
}