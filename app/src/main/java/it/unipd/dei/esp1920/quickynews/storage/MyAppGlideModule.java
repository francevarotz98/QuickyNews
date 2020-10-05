package it.unipd.dei.esp1920.quickynews.storage;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import it.unipd.dei.esp1920.quickynews.R;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 5; // 5 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
        builder.setDefaultRequestOptions(requestOptions(context));
    }

    private static RequestOptions requestOptions(Context context){
        return new RequestOptions()
                /*.signature(new ObjectKey(
                        System.currentTimeMillis() / (24 * 60 * 60 * 1000)))*/
                .override(500, 300)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .format(DecodeFormat.PREFER_RGB_565)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_baseline_photo_24);
    }
}
