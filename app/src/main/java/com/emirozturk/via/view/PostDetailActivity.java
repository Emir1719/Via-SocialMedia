package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emirozturk.via.R;
import com.emirozturk.via.adapter.PostAdapter;
import com.emirozturk.via.databinding.ActivityPostDetailBinding;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.FirebaseDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
   private ActivityPostDetailBinding binding;
   private PostAdapter postAdapter;
   private Post post;
   private IDatabase database;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      database = new FirebaseDB(this);

      Intent intent = getIntent();
      post = (Post) intent.getSerializableExtra("post");
      ArrayList<Post> posts = new ArrayList<>();
      posts.add(post);

      binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
      postAdapter = new PostAdapter(posts);
      binding.recyclerView.setAdapter(postAdapter);
      binding.btnDelete.setOnClickListener(this::delete);
   }

   public void delete(View view) {
      try {
         database.deletePost(post).thenAccept(result -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
         });
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}