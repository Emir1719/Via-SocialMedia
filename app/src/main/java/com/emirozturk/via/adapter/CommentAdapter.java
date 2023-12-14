package com.emirozturk.via.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emirozturk.via.databinding.RecyclerCommentRowBinding;
import com.emirozturk.via.databinding.RecyclerRowBinding;
import com.emirozturk.via.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
   private List<Comment> comments;

   public CommentAdapter(ArrayList<Comment> comments) {
      this.comments = comments;
   }

   static class ViewHolder extends RecyclerView.ViewHolder {
      private final RecyclerCommentRowBinding binding;

      public ViewHolder(RecyclerCommentRowBinding binding) {
         super(binding.getRoot());
         this.binding = binding;
      }
   }

   @NonNull @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      RecyclerCommentRowBinding binding = RecyclerCommentRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ViewHolder(binding);
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
      holder.binding.textEmail.setText(comments.get(i).getEmail());
      holder.binding.textComment.setText(comments.get(i).getMessage());
   }

   @Override
   public int getItemCount() {
      if (comments == null) {
         return 0;
      }
      return comments.size();
   }
}