package it.unipd.dei.esp1920.quickynews.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import it.unipd.dei.esp1920.quickynews.R;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder> {

    private final NewsApiResponse mNewsListContainer;
    private LayoutInflater mInflater;

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitle;
        final TextView mDescription;
        final TextView mDate;
        final NewsListAdapter mAdapter;

        ArticleViewHolder(View itemView, NewsListAdapter adapter) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_title);
            mDescription = itemView.findViewById(R.id.item_description);
            mDate = itemView.findViewById(R.id.item_date);
            mAdapter = adapter;
        }
    }

    public NewsListAdapter(Context context, NewsApiResponse newsListContainer) {
        mInflater = LayoutInflater.from(context);
        mNewsListContainer = newsListContainer;
    }

    @Override
    public NewsListAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.feed_list_item, parent, false);
        return new ArticleViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ArticleViewHolder holder, int position) {
        Article mCurrent = mNewsListContainer.getArticles().get(position);
        holder.mTitle.setText(mCurrent.getTitle());
        holder.mDescription.setText(mCurrent.getDescription());
        holder.mDate.setText(mCurrent.getPublishedAt());
    }

    @Override
    public int getItemCount() {
        if(mNewsListContainer==null)
            return 0;
        return mNewsListContainer.getArticles().size();
    }
}
