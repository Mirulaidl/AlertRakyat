package com.example.alertrakyat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapterActivity extends RecyclerView.Adapter<ArticleAdapterActivity.ArticleViewHolder> {

    private final List<Article> articles;
    private final Context context;

    public ArticleAdapterActivity(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.tagTextView.setText(article.getTag());
        holder.titleTextView.setText(article.getTitle());
        holder.descriptionTextView.setText(article.getDescription());

        // Handle click to open the URL
        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        final TextView tagTextView;
        final TextView titleTextView;
        final TextView descriptionTextView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.tagTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

}
}