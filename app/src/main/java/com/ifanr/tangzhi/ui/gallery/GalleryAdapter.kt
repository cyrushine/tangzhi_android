package com.ifanr.tangzhi.ui.gallery

import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ifanr.tangzhi.R
import com.ifanr.tangzhi.ext.gallery

class GalleryAdapter (
    private val items: List<String>,
    private val viewPager: ViewPager2
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 解决 ViewPager2 和 GestureImageView 手势冲突
    private val outerMatrix = Matrix()
    private val onOuterMatrixChangedListener = GestureImageView.OuterMatrixChangedListener {
        it.getOuterMatrix(outerMatrix)
        viewPager.isUserInputEnabled = outerMatrix.isIdentity
    }

    override fun getItemViewType(position: Int): Int =
        R.layout.gallery_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GalleryVH(parent, onOuterMatrixChangedListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val url = items[position]
        when (holder) {
            is GalleryVH -> {
                Glide.with(holder.image).gallery().load(url).into(holder.image)
            }
        }
    }
}

class GalleryVH (
    parent: ViewGroup,
    listener: GestureImageView.OuterMatrixChangedListener
): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)) {

    val image: GestureImageView = itemView as GestureImageView

    init {
        image.addOuterMatrixChangedListener(listener)
    }
}