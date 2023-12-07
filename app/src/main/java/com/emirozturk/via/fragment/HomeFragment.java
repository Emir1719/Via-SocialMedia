package com.emirozturk.via.fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirozturk.via.adapter.PostAdapter;
import com.emirozturk.via.databinding.FragmentHomeBinding;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.FirebaseDB;
import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {
   private FragmentHomeBinding binding;
   private ArrayList<Post> posts;
   private PostAdapter postAdapter;
   private IDatabase database;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      posts = new ArrayList<>();
      database = new FirebaseDB(requireContext());
      getData();
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentHomeBinding.inflate(inflater, container, false);
      //recyclerView'da alt alta göstereceğimizi linear ile söyledik.
      binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
      postAdapter = new PostAdapter(posts);
      binding.recyclerView.setAdapter(postAdapter);
      return binding.getRoot();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void getData() {
      try {
         database.getAllPost().thenAccept(posts1 -> {
            posts.addAll(posts1);
            postAdapter.notifyDataSetChanged();
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