package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emirozturk.via.R;
import com.emirozturk.via.adapter.PostAdapter;
import com.emirozturk.via.databinding.ActivityPostDetailBinding;
import com.emirozturk.via.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
   private ActivityPostDetailBinding binding;
   private PostAdapter postAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      Intent intent = getIntent();
      Post post = (Post) intent.getSerializableExtra("post");
      ArrayList<Post> posts = new ArrayList<>();
      posts.add(post);

      binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
      postAdapter = new PostAdapter(posts);
      binding.recyclerView.setAdapter(postAdapter);
      binding.btnDelete.setOnClickListener(this::delete);
   }

   public void delete(View view) {
      try {
         System.out.println("Silindi");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}