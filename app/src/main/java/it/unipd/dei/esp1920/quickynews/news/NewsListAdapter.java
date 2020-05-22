package it.unipd.dei.esp1920.quickynews.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.unipd.dei.esp1920.quickynews.R;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder> {

    private final NewsApiResponse mNewsListContainer;
    private LayoutInflater mInflater;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);


    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        final TextView mSource;
        final TextView mTitle;
        final TextView mDescription;
        final TextView mDate;
        final NewsListAdapter mAdapter;

        ArticleViewHolder(View itemView, NewsListAdapter adapter) {
            super(itemView);
            mSource = itemView.findViewById(R.id.item_source);
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
        holder.mSource.setText(mCurrent.getSource().getName());
        holder.mTitle.setText(mCurrent.getTitle());
        holder.mDescription.setText(mCurrent.getDescription());
        Date date = new Date();
        String now = formatter.format(date);
        try {
            Date articleDate = formatter.parse(mCurrent.getStringPublishedAt());
            Date fetchDate = formatter.parse(now);
            long millis = Math.abs(fetchDate.getTime() - articleDate.getTime());
            int minutes = (int) (millis / 1000)  / 60;
            double hours;
            if(minutes >= 60) {
                hours = minutes / 60.0;
                int h = (int) (hours + 0.5);
                if(h > 1)
                    holder.mDate.setText(h + " ore fa");
                else
                    holder.mDate.setText(h + " ora fa");
            }
            else if(minutes > 1)
                holder.mDate.setText(minutes + " minuti fa");
            else if(minutes == 1)
                holder.mDate.setText(minutes + " minuto fa");
            else if(minutes == 0)
            holder.mDate.setText("ora");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mNewsListContainer==null)
            return 0;
        return mNewsListContainer.getArticles().size();
    }
}
