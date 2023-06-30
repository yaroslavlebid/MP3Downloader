package com.yaroslavlebid.mp3downloader.di

import android.content.Context
import com.chaquo.python.Python
import com.google.gson.Gson
import com.tanodxyz.gdownload.DEF_MAX_THREADS_PER_EXECUTOR
import com.tanodxyz.gdownload.DownloadManager
import com.tanodxyz.gdownload.NetworkInfoProvider
import com.tanodxyz.gdownload.connection.ConnectionManagerImpl
import com.tanodxyz.gdownload.connection.URLConnectionFactory
import com.tanodxyz.gdownload.executors.ScheduledBackgroundExecutorImpl
import com.tanodxyz.gdownload.io.DefaultFileStorageHelper
import com.tanodxyz.gdownload.worker.DataReadWriteWorkersManagerImpl
import com.yaroslavlebid.mp3downloader.data.local.helpers.AudioConverterImpl
import com.yaroslavlebid.mp3downloader.data.local.helpers.FileSaverImpl
import com.yaroslavlebid.mp3downloader.data.mapper.YoutubeVideoMapper
import com.yaroslavlebid.mp3downloader.data.remote.DownloadFileApi
import com.yaroslavlebid.mp3downloader.data.remote.YoutubeDataSource
import com.yaroslavlebid.mp3downloader.data.remote.YoutubeDataSourceImpl
import com.yaroslavlebid.mp3downloader.data.remote.interceptors.NetworkConnectionInterceptor
import com.yaroslavlebid.mp3downloader.data.repository.DownloadFileRepositoryImpl
import com.yaroslavlebid.mp3downloader.data.repository.YoutubeVideoRepositoryImpl
import com.yaroslavlebid.mp3downloader.domain.helpers.AudioConverter
import com.yaroslavlebid.mp3downloader.domain.helpers.FileSaver
import com.yaroslavlebid.mp3downloader.domain.repostirory.DownloadFileRepository
import com.yaroslavlebid.mp3downloader.domain.repostirory.YoutubeVideoRepository
import com.yaroslavlebid.mp3downloader.util.DispatcherProvider
import com.yaroslavlebid.mp3downloader.util.DispatcherProviderImpl
import com.yaroslavlebid.mp3downloader.util.FileHelper
import com.yaroslavlebid.mp3downloader.util.FileHelperImpl
import com.yaroslavlebid.mp3downloader.util.ResourceProvider
import com.yaroslavlebid.mp3downloader.util.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)) // @Stream doesn't work with BODY level
            .addInterceptor(networkConnectionInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideFileDownloadApi(client: OkHttpClient): DownloadFileApi {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.google.com/") // it will be override by call
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providePython(): Python {
        return Python.getInstance()
    }

    @Provides
    @Singleton
    fun provideYoutubeDataSource(
        dispatcherProvider: DispatcherProvider,
        gson: Gson,
        python: Python
    ): YoutubeDataSource {
        return YoutubeDataSourceImpl(dispatcherProvider, gson, python)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl()
    }

    @Provides
    @Singleton
    fun provideYoutubeVideoRepository(
        youtubeDataSource: YoutubeDataSource,
        youtubeVideoMapper: YoutubeVideoMapper,
        resourceProvider: ResourceProvider
    ): YoutubeVideoRepository {
        return YoutubeVideoRepositoryImpl(youtubeDataSource, youtubeVideoMapper, resourceProvider)
    }

    @Provides
    fun provideFileHelper(
        @ApplicationContext context: Context
    ): FileHelper {
        return FileHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager {
        val defaultFileStorageHelper = DefaultFileStorageHelper(context)
        defaultFileStorageHelper.setFilesRoot(context.cacheDir)
        val urlConnectionFactory = URLConnectionFactory()
        val executor = ScheduledBackgroundExecutorImpl(corePoolSize = DEF_MAX_THREADS_PER_EXECUTOR)

        return DownloadManager.Builder(context).setNetworkInfoProvider(NetworkInfoProvider(context))
                .setDataReadWriteWorkersManager(DataReadWriteWorkersManagerImpl())
                .setCallbacksHandler(runCallbacksOnMainThread = false)
                .setStorageHelper(defaultFileStorageHelper)
                .setConnectionManager(ConnectionManagerImpl(urlConnectionFactory, executor))
                .setScheduledBackgroundExecutor(executor)
                .build()
    }
    @Provides
    @Singleton
    fun provideDownloadFileRepository(
        dispatcherProvider: DispatcherProvider,
        downloadManager: DownloadManager
    ): DownloadFileRepository {
        return DownloadFileRepositoryImpl(dispatcherProvider, downloadManager)
    }

    @Provides
    @Singleton
    fun provideAudioConverter(
        fileHelper: FileHelper,
        @ApplicationContext context: Context
    ): AudioConverter {
        return AudioConverterImpl(fileHelper, context)
    }

    @Provides
    @Singleton
    fun provideFileSaver(
        fileHelper: FileHelper,
        dispatcherProvider: DispatcherProvider
    ): FileSaver {
        return FileSaverImpl(fileHelper, dispatcherProvider)
    }
}