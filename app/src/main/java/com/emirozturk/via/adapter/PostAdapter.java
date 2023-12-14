package com.emirozturk.via.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.emirozturk.via.databinding.RecyclerRowBinding;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.FirebaseDB;
import com.emirozturk.via.view.CommentActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
   private final ArrayList<Post> posts;

   public PostAdapter(ArrayList<Post> posts) {
      this.posts = posts;
   }

   static class PostHolder extends RecyclerView.ViewHolder {
      private final RecyclerRowBinding binding;
      private final Context context;
      private final IDatabase database;
      private ArrayList<Post> posts;

      public PostHolder(RecyclerRowBinding binding, Context context, ArrayList<Post> posts) {
         super(binding.getRoot());
         this.binding = binding;
         this.context = context;
         this.posts = posts;
         this.database = new FirebaseDB(context);

         binding.btnLike.setOnClickListener(this::likePost);
         binding.btnComment.setOnClickListener(this::comment);
         binding.post.setOnClickListener(this::tabImage);
      }

      //Gönderiyi beğenme işlemini yapar.
      private void likePost(View view) {
         Post post = getCurrentPost();
         database.likePost(post).thenAccept(aBoolean -> {
            database.getLikeCount(post).thenAccept(binding.likeCount::setText);
         });
      }

      //Yorum sayfasına gider.
      private void comment(View view) {
         Intent intent = new Intent(context, CommentActivity.class);
         intent.putExtra("post", getCurrentPost());
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

      public Post getCurrentPost() {
         return posts.get(getAdapterPosition());
      }
   }

   @NonNull @Override
   public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      RecyclerRowBinding binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      Context context = parent.getContext();
      return new PostHolder(binding, context, posts);
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
      holder.binding.likeCount.setText(String.valueOf(posts.get(position).getLikeCount()));

      //Eğer kişi yorum girmediyse bu alan görünmesin ve yer kaplamasın.
      if (posts.get(position).getComment().trim().isEmpty()) {
         holder.binding.comment.setVisibility(View.GONE);
      }
   }
}