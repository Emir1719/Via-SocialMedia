package com.emirozturk.via.fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirozturk.via.adapter.UserPostAdapter;
import com.emirozturk.via.databinding.FragmentProfileBinding;
import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.AppAuth;
import com.emirozturk.via.service.FirebaseDB;
import com.emirozturk.via.widget.AppMessage;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
   private FragmentProfileBinding binding;
   private ArrayList<Post> posts;
   private UserPostAdapter postAdapter;
   private IAuth auth;
   private IDatabase database;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      posts = new ArrayList<>();
      auth = new AppAuth(requireContext());
      database = new FirebaseDB(requireContext());
      getData();
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentProfileBinding.inflate(inflater, container, false);
      binding.userPosts.setLayoutManager(new GridLayoutManager(requireContext(), 3));
      postAdapter = new UserPostAdapter(posts);
      binding.userPosts.setAdapter(postAdapter);
      binding.textEmail.setText(auth.currentUser().getEmail());
      return binding.getRoot();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void getData() {
      try {
         database.getPosts(auth.currentUser()).thenAccept(posts1 -> {
            posts.addAll(posts1);
            postAdapter.notifyDataSetChanged();
            binding.textPostCount.setText(String.valueOf(posts.size()));
         }).exceptionally(throwable -> {
            //Hata olursa
            AppMessage.show(requireContext(), throwable.getLocalizedMessage());
            return null;
         });
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}