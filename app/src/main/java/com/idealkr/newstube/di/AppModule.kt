package com.idealkr.newstube.di

import android.content.Context
import androidx.room.Room
import com.idealkr.newstube.data.api.NewsApi
import com.idealkr.newstube.data.db.VideoDatabase
import com.idealkr.newstube.domain.repository.VideoRepository
import com.idealkr.newstube.domain.use_case.*
import com.idealkr.newstube.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // Retrofit
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApiService(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    // Room
    @Singleton
    @Provides
    fun provideBookSearchDatabase(@ApplicationContext context: Context): VideoDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            VideoDatabase::class.java,
            "bookmark-videos"
        ).build()

    // UseCase
    @Singleton
    @Provides
    fun provideVideoUseCases(repository: VideoRepository): VideoUseCases {
        return VideoUseCases(
            getVideos = GetVideos(repository),
            getBookmarks = GetBookmarks(repository),
            getBookmark = GetBookmark(repository),
            insertBookmark = InsertBookmark(repository),
            deleteBookmark = DeleteBookmark(repository)
        )
    }
}