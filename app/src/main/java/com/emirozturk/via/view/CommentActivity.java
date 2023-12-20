package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.emirozturk.via.adapter.CommentAdapter;
import com.emirozturk.via.databinding.ActivityCommentBinding;
import com.emirozturk.via.model.Comment;
import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.AppAuth;
import com.emirozturk.via.service.FirebaseDB;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
   private ActivityCommentBinding binding;
   private CommentAdapter adapter;
   private ArrayList<Comment> comments;
   private Post post;
   private IAuth auth;
   private IDatabase database;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityCommentBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      Intent intent = getIntent();
      post = (Post) intent.getSerializableExtra("post");

      auth = new AppAuth(this);
      database = new FirebaseDB(this);
      comments = new ArrayList<>();
      binding.btnComment.setOnClickListener(this::sendComment);
      binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
      adapter = new CommentAdapter(comments);
      binding.recyclerView.setAdapter(adapter);
      getAllComment();
   }

   public void sendComment(View view) {
      Comment comment = new Comment();
      comment.setEmail(auth.currentUser().getEmail());
      comment.setMessage(binding.editTextComment.getText().toString());
      database.saveComment(comment, post).thenAccept(result -> {
         if (result) {
            getAllComment();
            binding.editTextComment.getText().clear();
         }
      });
   }

   @SuppressLint("NotifyDataSetChanged")
   public void getAllComment() {
      database.getAllComment(post).thenAccept(comments1 -> {
         comments.clear();
         comments.addAll(comments1);
         adapter.notifyDataSetChanged();
      });
   }
}