package com.emirozturk.via.service;
import android.content.Context;
import android.net.Uri;

import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.widget.AppMessage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

   private void updatePosts(Task<QuerySnapshot> task, CompletableFuture<ArrayList<Post>> future, ArrayList<Post> posts) {
      if (task.isSuccessful()) {
         for (DocumentSnapshot snapshot : task.getResult()) {
            Map<String, Object> map = snapshot.getData();
            Post post = new Post();
            post.fromMap(map);
            posts.add(post);
         }
         future.complete(posts); // Başarıyla tamamlandı.
      } else {
         future.completeExceptionally(task.getException());
      }
   }

   @Override
   ///Kullanıcının tüm gönderilerini getirir.
   public CompletableFuture<ArrayList<Post>> getPosts(FirebaseUser user) {
      CompletableFuture<ArrayList<Post>> future = new CompletableFuture<>();
      ArrayList<Post> posts = new ArrayList<>();

      firestore.collection("Posts")
              .whereEqualTo("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail())
              .orderBy("date", Query.Direction.DESCENDING)
              .get()
              .addOnCompleteListener(task -> updatePosts(task, future, posts));
      return future;
   }

   @Override
   ///Veritabanındaki tüm gönderileri getirir.
   public CompletableFuture<ArrayList<Post>> getAllPost() {
      //Firebaseden veri alma işlemini beklemek için future tanımlandı.
      CompletableFuture<ArrayList<Post>> future = new CompletableFuture<>();
      ArrayList<Post> posts = new ArrayList<>();

      firestore.collection("Posts")
              .orderBy("date", Query.Direction.DESCENDING)
              .get()
              .addOnCompleteListener(task -> updatePosts(task, future, posts));

      return future;
   }

   @Override
   public CompletableFuture<Boolean> deletePost(Post post) {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      StorageReference imageRef = storage.getReferenceFromUrl(post.getUrl());
      //Storagedeki resim başarılı bir şekilde silinirse ardından gönderiyi de sil.
      imageRef.delete().addOnSuccessListener(runnable -> {
         firestore.collection("Posts")
                 .whereEqualTo("url", post.getUrl())
                 .whereEqualTo("email", post.getEmail())
                 .get()
                 .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                       // Belge bulunduysa sil
                       for (DocumentSnapshot document : task.getResult().getDocuments()) {
                          System.out.println(document.getReference().getPath());
                          document.getReference().delete();
                       }
                       future.complete(true); // İşlem başarıyla tamamlandı
                    } else {
                       // Sorgu başarısız olduysa
                       future.completeExceptionally(task.getException());
                    }
                 });
      });
      return future;
   }
}