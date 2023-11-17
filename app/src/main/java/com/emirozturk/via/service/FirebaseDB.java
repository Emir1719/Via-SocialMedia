package com.emirozturk.via.service;
import android.content.Context;
import android.net.Uri;

import com.emirozturk.via.adapter.PostAdapter;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FirebaseDB implements IDatabase {
   private final Context context;
   private final FirebaseFirestore firestore;
   private final FirebaseStorage storage;
   private final FirebaseAuth auth;

   public FirebaseDB(Context context) {
      this.context = context;
      firestore = FirebaseFirestore.getInstance();
      storage = FirebaseStorage.getInstance();
      auth = FirebaseAuth.getInstance();
   }

   @Override
   public void uploadPost(Uri selectedImageUri, String comment) {
      try {
         UUID uuid = UUID.randomUUID();
         String imageName = uuid + ".jpg";
         StorageReference reference = storage.getReference();
         StorageReference imageRef = reference.child("images").child(imageName);
         imageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
            //image, storage'e yüklendikten sonra firestore'a eklemek için url'i alınır.
            StorageReference userImage = storage.getReference().child("images").child(imageName);
            userImage.getDownloadUrl().addOnSuccessListener(uri -> {
               String url = uri.toString();
               HashMap<String, Object> postMap = new HashMap<>();
               postMap.put("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail());
               postMap.put("url", url);
               postMap.put("comment", comment);
               postMap.put("likeCount", 0);
               postMap.put("date", Timestamp.now());

               firestore.collection("Posts").add(postMap).addOnSuccessListener(runnable -> {
                  AppMessage.show(context, "Resim başarıyla yüklendi.");
               }).addOnFailureListener(e -> {
                  AppMessage.show(context, e.getLocalizedMessage());
               });
            });
         }).addOnFailureListener(e -> {
            AppMessage.show(context, e.getLocalizedMessage());
         });
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public ArrayList<Post> getPosts(User user) {
      return null;
   }

   @Override
   public ArrayList<Post> getAllPost() {
      ArrayList<Post> posts = new ArrayList<>();
      firestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
         if (error != null) {
            AppMessage.show(context, error.getLocalizedMessage());
         }
         if (value != null) {
            for (DocumentSnapshot snapshot : value.getDocuments()) {
               Map<String, Object> map = snapshot.getData();
               Post post = new Post();
               post.mapToPost(map);
               posts.add(post);
            }
         }
      });
      return posts;
   }
}