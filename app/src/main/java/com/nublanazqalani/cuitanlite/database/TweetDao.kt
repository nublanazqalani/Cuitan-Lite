package com.nublanazqalani.cuitanlite.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nublanazqalani.cuitanlite.model.Tweet

@Dao
interface TweetDao {
    @Query("SELECT * FROM tweet_table")
    fun getTweets(): LiveData<List<Tweet>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTweet(tweet: Tweet)

    @Update
    suspend fun updateTweet(tweet: Tweet)

    @Delete
    suspend fun deleteTweet(tweet: Tweet)
}