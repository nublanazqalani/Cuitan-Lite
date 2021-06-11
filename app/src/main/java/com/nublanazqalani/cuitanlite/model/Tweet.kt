package com.nublanazqalani.cuitanlite.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tweet_table")
data class Tweet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "tweet")
    var tweet: String,

    @ColumnInfo(name = "date_created")
    var dateCreated: String,

    @ColumnInfo(name = "date_updated")
    var dateUpdated: String
)