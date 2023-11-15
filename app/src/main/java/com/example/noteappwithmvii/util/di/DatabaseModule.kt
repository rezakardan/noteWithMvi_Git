package com.example.noteappwithmvii.util.di

import android.content.Context
import androidx.room.Room
import com.example.noteappwithmvii.data.database.NoteDatabase
import com.example.noteappwithmvii.data.model.NoteEntity
import com.example.noteappwithmvii.util.ROOM_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideEntity() = NoteEntity()


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NoteDatabase::class.java, ROOM_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()



    @Provides
    @Singleton
    fun provideDao(db: NoteDatabase)=db.noteDao()


}