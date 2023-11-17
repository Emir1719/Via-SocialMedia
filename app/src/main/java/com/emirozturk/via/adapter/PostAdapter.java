package com.emirozturk.via.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emirozturk.via.databinding.RecyclerRowBinding;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.view.CommentActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
   private final ArrayList<Post> posts;
   private Context context;

   public PostAdapter(ArrayList<Post> posts) {
      this.posts = posts;
   }

   static class PostHolder extends RecyclerView.ViewHolder {
      private final RecyclerRowBinding binding;
      private final Context context;

      public PostHolder(RecyclerRowBinding binding, Context context) {
         super(binding.getRoot());
         this.binding = binding;
         this.context = context;
         binding.btnLike.setOnClickListener(this::likePost);
         binding.btnOptions.setOnClickListener(this::option);
         binding.btnComment.setOnClickListener(this::comment);
      }

      private void likePost(View view) {
         String likeText = binding.likeCount.getText().toString();
         int count = Integer.parseInt(likeText);
         count++;
         binding.likeCount.setText(String.valueOf(count));
      }

      private void option(View view) {
         System.out.println("deneme");
      }

      private void comment(View view) {
         Intent intent = new Intent(context, CommentActivity.class);
         context.startActivity(intent);
      }
   }

   @NonNull @Override
   public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      RecyclerRowBinding binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      context = parent.getContext();
      return new PostHolder(binding, context);
   }

   @Override
   public int getItemCount() {
      return posts.size();
   }

   @Override
   public void onBindViewHolder(@NonNull PostHolder holder, int position) {
      holder.binding.email.setText(posts.get(position).getEmail());
      holder.binding.comment.setText(posts.get(position).getComment());
      Picasso.get().load(posts.get(position).getUrl()).into(holder.binding.post);
   }
}