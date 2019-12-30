package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.ui.LaunchActivity
import com.ifanr.tangzhi.ui.browser.BrowserActivity
import com.ifanr.tangzhi.ui.browser.BrowserModule
import com.ifanr.tangzhi.ui.comment.CommentActivity
import com.ifanr.tangzhi.ui.comment.CommentModule
import com.ifanr.tangzhi.ui.gallery.GalleryActivity
import com.ifanr.tangzhi.ui.gallery.GalleryModule
import com.ifanr.tangzhi.ui.gallery.GalleryViewModel
import com.ifanr.tangzhi.ui.postlist.PostListActivity
import com.ifanr.tangzhi.ui.postlist.PostListModule
import com.ifanr.tangzhi.ui.product.ProductActivity
import com.ifanr.tangzhi.ui.product.ProductModule
import com.ifanr.tangzhi.ui.product.comments.review.ProductReviewModule
import com.ifanr.tangzhi.ui.product.comments.review.ProductTagDialogFragment
import com.ifanr.tangzhi.ui.product.comments.review.ReviewFragment
import com.ifanr.tangzhi.ui.product.indexes.IndexesDialogFragment
import com.ifanr.tangzhi.ui.productlist.ProductListActivity
import com.ifanr.tangzhi.ui.productlist.ProductListModule
import com.ifanr.tangzhi.ui.productparam.ProductParamActivity
import com.ifanr.tangzhi.ui.productparam.ProductParamModule
import com.ifanr.tangzhi.ui.relatedproducts.RelatedProductsActivity
import com.ifanr.tangzhi.ui.relatedproducts.RelatedProductsModule
import com.ifanr.tangzhi.ui.relatedproducts.RelatedProductsViewModel
import com.ifanr.tangzhi.ui.sendcomment.SendCommentActivity
import com.ifanr.tangzhi.ui.sendcomment.SendCommentModule
import com.ifanr.tangzhi.ui.sendreview.SendReviewActivity
import com.ifanr.tangzhi.ui.sendreview.SendReviewModule
import com.ifanr.tangzhi.ui.sendreview.SendReviewViewModel
import com.ifanr.tangzhi.ui.share.ShareActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [CommentModule::class])
    abstract fun commentActivity(): CommentActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SendCommentModule::class])
    abstract fun sendCommentActivity(): SendCommentActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [SendReviewModule::class])
    abstract fun sendReviewActivity(): SendReviewActivity

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract fun indexesDialogFragment(): IndexesDialogFragment

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProductReviewModule::class])
    abstract fun productTagDialogFragment(): ProductTagDialogFragment

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProductReviewModule::class])
    abstract fun reviewFragment(): ReviewFragment

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun shareActivity(): ShareActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [GalleryModule::class])
    abstract fun galleryActivity(): GalleryActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProductListModule::class])
    abstract fun productListActivity(): ProductListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RelatedProductsModule::class])
    abstract fun relatedProductsActivity(): RelatedProductsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PostListModule::class])
    abstract fun postListActivity(): PostListActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [BrowserModule::class])
    abstract fun browserActivity(): BrowserActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProductParamModule::class])
    abstract fun productParamActivity(): ProductParamActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProductModule::class])
    abstract fun productActivity(): ProductActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun launchActivity(): LaunchActivity
}