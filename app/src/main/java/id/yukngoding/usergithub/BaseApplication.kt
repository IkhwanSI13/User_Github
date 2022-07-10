package id.yukngoding.usergithub

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import id.yukngoding.usergithub.models.UserCoilMapper

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .componentRegistry { add(UserCoilMapper()) }
            .build()

}