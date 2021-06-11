package com.nublanazqalani.cuitanlite.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.nublanazqalani.cuitanlite.model.Tweet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TweetRepository(application: Application){
    private val tweetDao: TweetDao?
    private var tweet: LiveData<List<Tweet>>? = null

    init{
        val db = TweetDatabase.getInstance(application.applicationContext)
        tweetDao = db?.tweetDao()
        tweet = tweetDao?.getTweets()
    }

    fun getTweets(): LiveData<List<Tweet>>?{
        return tweet
    }

    fun insert(tweet: Tweet) = runBlocking {
        this.launch(Dispatchers.IO){
            tweetDao?.insertTweet(tweet)
        }
    }

    fun update(tweet: Tweet) = runBlocking {
        this.launch(Dispatchers.IO){
            tweetDao?.updateTweet(tweet)
        }
    }

    fun delete(tweet: Tweet) = runBlocking {
        this.launch(Dispatchers.IO){
            tweetDao?.deleteTweet(tweet)
        }
    }

}