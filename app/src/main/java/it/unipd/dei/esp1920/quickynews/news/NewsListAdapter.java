package it.unipd.dei.esp1920.quickynews.news;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.unipd.dei.esp1920.quickynews.NewsDetailActivity;
import it.unipd.dei.esp1920.quickynews.R;
import it.unipd.dei.esp1920.quickynews.fragments.TopNews;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder> {

    private static final String TAG = "NewsListAdapter";

    private final NewsApiResponse mNewsListContainer;
    private LayoutInflater mInflater;
    // private Context mContext;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    private MyRepository myRepository = TopNews.getRepository();
    private List<Article> mListArticle;

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        final TextView mSource;
        final TextView mTitle;
        final TextView mDescription;
        final TextView mDate;
        final NewsListAdapter mAdapter;
        final View mView;
        final ImageView mImageView;

        ArticleViewHolder(View itemView, NewsListAdapter adapter) {
            super(itemView);
            mView = itemView;
            mSource = itemView.findViewById(R.id.item_source);
            mTitle = itemView.findViewById(R.id.item_title);
            mDescription = itemView.findViewById(R.id.item_description);
            mDate = itemView.findViewById(R.id.item_date);
            mImageView=itemView.findViewById(R.id.item_img);
            mAdapter = adapter;
        }
    }

    public NewsListAdapter() {
        mNewsListContainer = null;
        //mInflater = LayoutInflater.from(c);
    }


    public NewsListAdapter(/*Context context, */ NewsApiResponse newsListContainer) {
        // mInflater = LayoutInflater.from(context);
        mNewsListContainer = newsListContainer;
    }

    @Override
    public NewsListAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item_new, parent, false);
        return new ArticleViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ArticleViewHolder holder, int position) {
        //Log.d(TAG,"onBindViewHolder()");

        Article mCurrent = mNewsListContainer.getArticles().get(position);
        holder.mSource.setText(mCurrent.getSource().getName());
        holder.mTitle.setText(mCurrent.getTitle());
        holder.mDescription.setText(mCurrent.getDescription());
        Picasso.get().load(mCurrent.getUrlToImage()).into(holder.mImageView);
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
                    holder.mDate.setText(h + " hours ago");
                else
                    holder.mDate.setText(h + " hour ago");
            }
            else if(minutes > 1)
                holder.mDate.setText(minutes + " minutes ago");
            else if(minutes == 1)
                holder.mDate.setText(minutes + " minute ago");
            else if(minutes == 0)
            holder.mDate.setText("now");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick()");
                // Send an intent to the NewsDetailActivity
                Context context = v.getContext();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url", mCurrent.getUrl());

                if(mCurrent.getSource().getId()!=null)
                    intent.putExtra("source_id", mCurrent.getSource().getId());
                else{
                    intent.putExtra("source_id",myRepository.getId(mCurrent.getUrl())); //provo a vedere se ho la news nel db
                }
                Log.d(TAG,"mCurrent.getUrl() ="+mCurrent.getUrl());
                Log.d(TAG,"mCurrent.getSource.getid ="+mCurrent.getSource().getId());
                Log.d(TAG,"myRep.getId(url) ="+myRepository.getId(mCurrent.getUrl()));
                context.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"onLongClick()");
                final CharSequence[] items = {"Yes", "No"};
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                if(!myRepository.isFavoriteArticle(mCurrent.getUrl())) //la notizia NON era salvata
                    builder.setTitle("Do you want to save this news?");
                else //la notizia era già stata salvata
                    builder.setTitle("Do you want to keep saved this news?");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        Log.d(TAG,"onClick() of onLongClick()");
                        Log.d(TAG,"Title of Article long-pressed  = "+mCurrent.getTitle());
                        boolean wasFavorite=myRepository.isFavoriteArticle(mCurrent.getUrl());
                        if(items[item]=="Yes"){ //Yes
                            myRepository.setFavorite(mCurrent.getUrl(),true);
                            //Log.d(TAG,"^^^^^ mCurrent.getid() ="+mCurrent.getSource().getId());
                            myRepository.setId(mCurrent.getUrl(),mCurrent.getSource().getId());
                            if(!wasFavorite){
                                Toast toast= Toast.makeText(v.getContext(),"You have saved your news.\n Now you can find it on Saved.",Toast.LENGTH_LONG);
                                //toast.setGravity(0,0,0);
                                toast.show();

                                 new GetNewsTask(v.getContext(), new GetNewsTask.AsyncResponse() {
                                    @Override
                                    public void processFinish(ArrayList<String> output) {
                                        Log.d(TAG,"processFinish() di onClick()");

                                        String page="";
                                        if(output!=null) {
                                            for(String word : output)
                                                page+=word;
                                            myRepository.setPageHTML(mCurrent.getUrl(), page);
                                        }

                                    }
                                }).execute(mCurrent.getUrl(),mCurrent.getSource().getId());

                            }

                            else{
                                Toast toast= Toast.makeText(v.getContext(),"Keep calm.\n You still have your news on Saved.",Toast.LENGTH_LONG);
                                //t.setGravity(Gravity.CENTER_HORIZONTAL,15,10);
                                toast.show();
                            }



                        }
                        else //no
                            myRepository.setFavorite(mCurrent.getUrl(),false);
                        Log.d(TAG,"All favorites  = "+myRepository.getFavoritesArticle().size());

                    }//{[\m]onClick}
                });
                builder.show();
                return true;
            }

        });


    }

    public void setArticle(List<Article> articles){
        mListArticle = articles;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mNewsListContainer==null)
            return 0;
        return mNewsListContainer.getArticles().size();
    }
}
