package com.emirozturk.via.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emirozturk.via.databinding.UserPostCellBinding;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.Holder> {
   private final ArrayList<Post> posts;

   ///Kullanıcının gönderileri alınır.
   public UserPostAdapter(ArrayList<Post> posts) {
      this.posts = posts;
   }

   static class Holder extends RecyclerView.ViewHolder {
      private final UserPostCellBinding binding;
      private final Context context;

      public Holder(UserPostCellBinding binding, Context context) {
         super(binding.getRoot());
         this.binding = binding;
         this.context = context;
      }
   }

   @NonNull @Override
   public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      UserPostCellBinding binding = UserPostCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      Context context = parent.getContext();
      return new Holder(binding, context);
   }

   @Override
   public void onBindViewHolder(@NonNull Holder holder, int position) {
      //resim view'e aktarılır.
      Picasso.get().load(posts.get(position).getUrl()).into(holder.binding.post);
   }

   @Override
   public int getItemCount() {
      return posts.size();
   }
}