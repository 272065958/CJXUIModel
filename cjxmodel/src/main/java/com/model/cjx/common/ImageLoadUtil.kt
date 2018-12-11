package com.model.cjx.common

import android.app.Activity
import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.text.TextUtils
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget

/**
 * Created by cjx on 17-6-8.
 * 图片加载工具 用Glide插件
 */

object ImageLoadUtil {
    private val options: RequestOptions = RequestOptions()

    // 返回服务器图片完整路径
    fun setImage(context: Activity, imageView: ImageView, path: String?, errorRes: Int = -1) {
        if (context.isFinishing) {
            return
        }
        if (!TextUtils.isEmpty(path)) {
            if (errorRes > 0) {
                Glide.with(context).load(path).apply(options.error(errorRes).placeholder(errorRes)).into(imageView)
            } else {
                Glide.with(context).load(path).into(imageView)
            }
        } else {
            imageView.setImageBitmap(null)
        }
    }

    // 返回服务器图片完整路径
    fun setHeadImage(context: Activity, imageView: ImageView, path: String?, errorRes: Int = -1) {
        if (context.isFinishing) {
            return
        }
        if (!TextUtils.isEmpty(path)) {
            val builder = Glide.with(context).asBitmap().load(path)
            if (errorRes > 0) {
                builder.apply(options.error(errorRes).placeholder(errorRes))
            }
            builder.into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                    circularBitmapDrawable.isCircular = true
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
        } else if (errorRes > 0) {
            imageView.setImageResource(errorRes)
        } else {
            imageView.setImageBitmap(null)
        }
    }

}
