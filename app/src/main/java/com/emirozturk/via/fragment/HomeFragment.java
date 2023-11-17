package com.emirozturk.via.fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirozturk.via.R;
import com.emirozturk.via.adapter.PostAdapter;
import com.emirozturk.via.databinding.FragmentAddPostBinding;
import com.emirozturk.via.databinding.FragmentHomeBinding;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.service.FirebaseDB;
import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {
   private FragmentHomeBinding binding;
   private FirebaseFirestore firestore;
   private ArrayList<Post> posts;
   private PostAdapter postAdapter;
   private IDatabase database;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      firestore = FirebaseFirestore.getInstance();
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

   private void getData() {
      try {
         firestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if (error != null) {
               AppMessage.show(requireContext(), error.getLocalizedMessage());
            }
            if (value != null) {
               for (DocumentSnapshot snapshot : value.getDocuments()) {
                  Map<String, Object> map = snapshot.getData();
                  Post post = new Post();
                  post.mapToPost(map);
                  posts.add(post);
               }
               postAdapter.notifyDataSetChanged();
            }
         });
         /*ArrayList<Post> postList = database.getAllPost();
         posts.addAll(postList);
         postAdapter.notifyDataSetChanged();*/
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}