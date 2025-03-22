package com.znextapp.interactivetaskmanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.znextapp.interactivetaskmanager.database.TaskDatabase
import com.znextapp.interactivetaskmanager.appdao.TaskDao
import com.znextapp.interactivetaskmanager.data.ThemePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context,
            TaskDatabase::class.java,
            "task_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideThemePreference(@ApplicationContext context: Context): ThemePreference {
        return ThemePreference(context)
    }
}

// Migration object
val MIGRATION_1_2 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE tasks ADD COLUMN due_date TEXT NOT NULL DEFAULT ''")
    }
}
