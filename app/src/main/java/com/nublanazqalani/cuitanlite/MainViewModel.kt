package com.nublanazqalani.cuitanlite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nublanazqalani.cuitanlite.database.TweetRepository
import com.nublanazqalani.cuitanlite.model.Tweet

class MainViewModel(application: Application): AndroidViewModel(application) {
    private var tweetRepository = TweetRepository(application)
    private var tweets: LiveData<List<Tweet>>? = tweetRepository.getTweets()


    fun getTweets(): LiveData<List<Tweet>>? {
        return tweets
    }

    fun insertTweet(tweet: Tweet) {
        tweetRepository.insert(tweet)
    }

    fun deleteTweet(tweet: Tweet) {
        tweetRepository.delete(tweet)
    }

    fun updateTweet(tweet: Tweet) {
        tweetRepository.update(tweet)
    }
}