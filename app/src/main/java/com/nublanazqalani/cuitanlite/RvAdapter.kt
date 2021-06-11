package com.nublanazqalani.cuitanlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.nublanazqalani.cuitanlite.model.Tweet
import com.nublanazqalani.cuitanlite.utils.Commons
import kotlinx.android.synthetic.main.item_empty.view.*
import kotlinx.android.synthetic.main.item_tweet.view.*
import java.text.SimpleDateFormat
import java.util.*

class RvAdapter(private val listener: (Tweet, Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val VIEW_EMPTY_DATA = 0
    private val VIEW_TWEET = 1

    private var tweetList = listOf<Tweet>()
    private var tweetSearchResult = listOf<Tweet>()

    fun setTweetList(tweetList: List<Tweet>) {
        this.tweetList = tweetList
        this.tweetSearchResult = tweetList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val keywords = constraint.toString()
                if (keywords.isEmpty())
                    tweetSearchResult = tweetList
                else{
                    val filteredList = ArrayList<Tweet>()
                    for (tweet in tweetList) {
                        if (tweet.toString().toLowerCase(Locale.ROOT).contains(keywords.toLowerCase(Locale.ROOT)))
                            filteredList.add(tweet)
                    }
                    tweetSearchResult = filteredList
                }

                val searchResults = FilterResults()
                searchResults.values = tweetSearchResult
                return  searchResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                tweetSearchResult = results?.values as List<Tweet>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (tweetSearchResult.isEmpty())
            VIEW_EMPTY_DATA
        else
            VIEW_TWEET
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {
            VIEW_TWEET -> TweetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false))
            VIEW_EMPTY_DATA -> EmptyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false))
            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount(): Int = if (tweetSearchResult.isEmpty()) 1 else tweetSearchResult.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_EMPTY_DATA -> {
                val emptyHolder = holder as EmptyViewHolder
                emptyHolder.bindItem()
            }
            VIEW_TWEET -> {
                val tweetHolder = holder as TweetViewHolder
                val sortedList = tweetSearchResult.sortedWith(compareBy({it.dateCreated}, {it.dateUpdated}))
                tweetHolder.bindItem(sortedList[position], listener)
            }
        }
    }

    class TweetViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(tweet: Tweet, listener: (Tweet, Int) -> Unit) {
            val parsedDateCreated = SimpleDateFormat("dd/MM/yy", Locale.US).parse(tweet.dateCreated) as Date
            val dateCreated = Commons.formatDate(parsedDateCreated, "EEE, dd MMM yyyy")

            val parsedDateUpdated = SimpleDateFormat("dd/MM/yy", Locale.US).parse(tweet.dateCreated) as Date
            val dateUpdated = Commons.formatDate(parsedDateUpdated, "EEE, dd MMM yyyy")

            val date = if (tweet.dateUpdated != tweet.dateCreated) dateUpdated else dateCreated

            itemView.tv_title.text = tweet.tweet
            itemView.tv_date_created_updated.text = date

            itemView.setOnClickListener{
                listener(tweet, layoutPosition)
            }
        }
    }

    class EmptyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItem(){
            itemView.tv_empty.text = "Upss! Anda belum memiliki cuitan."
        }
    }
}