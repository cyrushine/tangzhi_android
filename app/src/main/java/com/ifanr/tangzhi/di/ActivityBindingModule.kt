package com.ifanr.tangzhi.di

import com.ifanr.tangzhi.route.SignInInterceptor
import com.ifanr.tangzhi.ui.LaunchActivity
import com.ifanr.tangzhi.ui.browser.BrowserActivity
import com.ifanr.tangzhi.ui.browser.BrowserModule
import com.ifanr.tangzhi.ui.comment.CommentActivity
import com.ifanr.tangzhi.ui.comment.CommentModule
import com.ifanr.tangzhi.ui.follows.FollowsActivity
import com.ifanr.tangzhi.ui.follows.FollowsModule
import com.ifanr.tangzhi.ui.gallery.GalleryActivity
import com.ifanr.tangzhi.ui.gallery.GalleryModule
import com.ifanr.tangzhi.ui.index.IndexActivity
import com.ifanr.tangzhi.ui.index.IndexModule
import com.ifanr.tangzhi.ui.index.home.HomeFragment
import com.ifanr.tangzhi.ui.index.home.HomeModule
import com.ifanr.tangzhi.ui.index.profile.ProfileFragment
import com.ifanr.tangzhi.ui.index.profile.ProfileModule
import com.ifanr.tangzhi.ui.message.MessageActivity
import com.ifanr.tangzhi.ui.message.MessageModule
import com.ifanr.tangzhi.ui.message.fragment.MessagePageFragment
import com.ifanr.tangzhi.ui.message.fragment.MessagePageModule
import com.ifanr.tangzhi.ui.points.PointsActivity
import com.ifanr.tangzhi.ui.points.PointsModule
import com.ifanr.tangzhi.ui.points.handbook.PointHandBookDialogFragment
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
import com.ifanr.tangzhi.ui.search.SearchActivity
import com.ifanr.tangzhi.ui.search.SearchModule
import com.ifanr.tangzhi.ui.sendcomment.SendCommentActivity
import com.ifanr.tangzhi.ui.sendcomment.SendCommentModule
import com.ifanr.tangzhi.ui.sendreview.SendReviewActivity
import com.ifanr.tangzhi.ui.sendreview.SendReviewModule
import com.ifanr.tangzhi.ui.share.ShareActivity
import com.ifanr.tangzhi.ui.share.ShareModule
import com.ifanr.tangzhi.ui.signin.wechat.SignInByWechatActivity
import com.ifanr.tangzhi.ui.signin.wechat.SignInByWechatModule
import com.ifanr.tangzhi.ui.signin.bindlocalphone.BindLocalPhoneActivity
import com.ifanr.tangzhi.ui.signin.bindlocalphone.BindLocalPhoneModule
import com.ifanr.tangzhi.ui.signin.bindphone.BindPhoneActivity
import com.ifanr.tangzhi.ui.signin.bindphone.BindPhoneModule
import com.ifanr.tangzhi.ui.signin.email.SignInByEmailActivity
import com.ifanr.tangzhi.ui.signin.email.SignInByEmailModule
import com.ifanr.tangzhi.ui.signin.phone.SignInByPhoneActivity
import com.ifanr.tangzhi.ui.signin.phone.SignInByPhoneModule
import com.ifanr.tangzhi.ui.updateprofile.UpdateProfileActivity
import com.ifanr.tangzhi.ui.updateprofile.UpdateProfileModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {


    @ActivityScoped
    @ContributesAndroidInjector(modules = [FollowsModule::class])
    abstract fun followsActivity(): FollowsActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [MessagePageModule::class])
    abstract fun messagePageFragment(): MessagePageFragment

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MessageModule::class])
    abstract fun messageActivity(): MessageActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [BindPhoneModule::class])
    abstract fun BindPhoneActivity(): BindPhoneActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [BindLocalPhoneModule::class])
    abstract fun bindLocalPhoneActivity(): BindLocalPhoneActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SignInByEmailModule::class])
    abstract fun signInByEmailActivity(): SignInByEmailActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SignInByPhoneModule::class])
    abstract fun signInByPhoneActivity(): SignInByPhoneActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun pointHandBookDialogFragment(): PointHandBookDialogFragment

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PointsModule::class])
    abstract fun pointsActivity(): PointsActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun SignInInterceptor(): SignInInterceptor

    @ActivityScoped
    @ContributesAndroidInjector(modules = [UpdateProfileModule::class])
    abstract fun updateProfileActivity(): UpdateProfileActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SignInByWechatModule::class])
    abstract fun signInActivity(): SignInByWechatActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun profileFragment(): ProfileFragment

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun searchActivity(): SearchActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun homeFragment(): HomeFragment

    @ActivityScoped
    @ContributesAndroidInjector(modules = [IndexModule::class])
    abstract fun indexActivity(): IndexActivity

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
    @ContributesAndroidInjector(modules = [ShareModule::class])
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
    @ContributesAndroidInjector(modules = [ProductModule::class, ProductReviewModule::class])
    abstract fun productActivity(): ProductActivity

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun launchActivity(): LaunchActivity
}