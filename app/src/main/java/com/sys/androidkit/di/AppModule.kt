package com.sys.androidkit.di

import android.content.Context
import com.sys.androidkit.core.database.AppDatabase
import com.sys.androidkit.core.database.NoteDao
import com.sys.androidkit.core.datastore.AppPreferences
import com.sys.androidkit.core.network.NetworkModule
import com.sys.androidkit.feature.network.PostApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context,
    ): AppPreferences = AppPreferences(context)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun providePostApi(): PostApi = NetworkModule.createApi()
}
