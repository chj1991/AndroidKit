package com.sys.androidkit.feature.storage.mmkv

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MmkvModule {

    @Provides
    @Singleton
    fun provideMmkvStore(
        @ApplicationContext context: Context,
    ): MmkvStore {
        MmkvInitializer.init(context)
        return MmkvStore()
    }
}
