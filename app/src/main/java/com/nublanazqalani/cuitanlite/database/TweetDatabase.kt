package com.nublanazqalani.cuitanlite.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nublanazqalani.cuitanlite.model.Tweet

@Database(entities = [Tweet::class], exportSchema = false, version = 1)
abstract class TweetDatabase : RoomDatabase() {
    abstract fun tweetDao(): TweetDao

    companion object{
        private const val DB_NAME = "TWEET_DATABASE"
        private var instance: TweetDatabase? = null

        fun getInstance(context: Context): TweetDatabase? {
            if (instance == null) {
                synchronized(TweetDatabase::class) {
                    instance = Room.databaseBuilder(context, TweetDatabase::class.java, DB_NAME).build()
                }
            }

            return instance
        }
    }

}