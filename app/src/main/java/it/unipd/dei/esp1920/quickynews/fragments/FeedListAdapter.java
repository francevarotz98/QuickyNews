package it.unipd.dei.esp1920.quickynews.fragments;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.fetch.Item;

class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ItemViewHolder> {

    private final LinkedList<Item> mFeedList;
    private LayoutInflater mInflater;

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitle;
        final TextView mDescription;
        final TextView mDate;
        final FeedListAdapter mAdapter;

        public ItemViewHolder(View itemView, FeedListAdapter adapter) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_title);
            mDescription = itemView.findViewById(R.id.item_description);
            mDate = itemView.findViewById(R.id.item_date);
            mAdapter = adapter;
        }
    }

    public FeedListAdapter(Context context, LinkedList<Item> feedList) {
        mInflater = LayoutInflater.from(context);
        mFeedList = feedList;
    }

    @Override
    public FeedListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.feed_list_item, parent, false);
        return new ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(FeedListAdapter.ItemViewHolder holder, int position) {
        Item mCurrent = mFeedList.get(position);
        holder.mTitle.setText(mCurrent.getTitle());
        holder.mDescription.setText(mCurrent.getDescription());
        holder.mDate.setText(mCurrent.getDate());
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }
}
