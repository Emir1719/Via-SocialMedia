package com.emirozturk.via.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emirozturk.via.R;
import com.emirozturk.via.databinding.RecyclerRowBinding;
import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.AppAuth;
import com.emirozturk.via.view.CommentActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
   private final ArrayList<Post> posts;

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
         binding.btnComment.setOnClickListener(this::comment);
         binding.post.setOnClickListener(this::tabImage);
      }

      //Gönderiyi beğenme işlemini yapar.
      private void likePost(View view) {
         String likeText = binding.likeCount.getText().toString();
         int count = Integer.parseInt(likeText);
         count++;
         binding.likeCount.setText(String.valueOf(count));
         binding.btnLike.setEnabled(false);
      }

      //Yorum sayfasına gider.
      private void comment(View view) {
         Intent intent = new Intent(context, CommentActivity.class);
         context.startActivity(intent);
      }

      //Resmin bütün olarak görüntülenmesini sağlar.
      private void tabImage(View view) {
         ImageView.ScaleType scaleType = binding.post.getScaleType();
         switch (scaleType) {
            case CENTER_CROP:
               scaleType = ImageView.ScaleType.FIT_CENTER;
               break;
            case FIT_CENTER:
               scaleType = ImageView.ScaleType.CENTER_CROP;
               break;
         }
         binding.post.setScaleType(scaleType);
      }
   }

   @NonNull @Override
   public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      RecyclerRowBinding binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      Context context = parent.getContext();
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

      //Eğer kişi yorum girmediyse bu alan görünmesin ve yer kaplamasın.
      if (posts.get(position).getComment().trim().isEmpty()) {
         holder.binding.comment.setVisibility(View.GONE);
      }
   }
}