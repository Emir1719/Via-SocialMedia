package com.emirozturk.via.service;
import android.content.Context;
import android.net.Uri;

import com.emirozturk.via.model.Comment;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.model.Post;
import com.emirozturk.via.widget.AppMessage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

   @Override
   public CompletableFuture<Boolean> likePost(Post post) {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      hasPostLiked(post).thenAccept(result -> {
         if (result) {
            //Gönderi beğenilmediyse beğen.
            changePostLike(post, 1);
            createLikesDoc(post);
         }
         else {
            //Gönderi beğenildiyse beğenmeyi geri al.
            changePostLike(post, -1);
            deleteLikesDoc(post);
         }
         future.complete(true);
      });
      return future;
   }

   @Override
   public CompletableFuture<String> getLikeCount(Post post) {
      CompletableFuture<String> future = new CompletableFuture<>();
      firestore.collection("Posts")
              .whereEqualTo("url", post.getUrl()).get()
              .addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       // Belge bulununca belge id'yi al.
                       String documentId = document.getId();

                       // Belge kimliği kullanarak likeCount değerini getir.
                       firestore.collection("Posts").document(documentId).get()
                               .addOnCompleteListener(likeCountTask -> {
                                  if (likeCountTask.isSuccessful()) {
                                     //Gönderinin like sayısını getirir.
                                     String likeCount = likeCountTask.getResult().get("likeCount").toString();
                                     future.complete(likeCount);
                                  } else {
                                     future.completeExceptionally(likeCountTask.getException());
                                  }
                               });
                    }
                 }
              });
      return future;
   }

   @Override
   //İlgili gönderiye ait yorumları getirir.
   public CompletableFuture<ArrayList<Comment>> getAllComment(Post post) {
      CompletableFuture<ArrayList<Comment>> future = new CompletableFuture<>();
      ArrayList<Comment> comments = new ArrayList<>();

      firestore.collection("Comments")
              .whereEqualTo("postUrl", post.getUrl())
              .orderBy("date", Query.Direction.ASCENDING)
              .get()
              .addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                       Map<String, Object> map = snapshot.getData();
                       Comment comment = new Comment();
                       comment.fromMap(map);
                       comments.add(comment);
                    }
                    future.complete(comments);
                 }
              });

      return future;
   }

   @Override
   //İlgili gönderiye ait yorumu kaydeder.
   public CompletableFuture<Boolean> saveComment(Comment comment, Post post) {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      Map<String, Object> map = comment.toMap();
      map.put("postUrl", post.getUrl());
      map.put("date", Timestamp.now());
      firestore.collection("Comments").add(map).addOnSuccessListener(runnable -> {
         future.complete(true);
      });
      return future;
   }

   private void deleteLikesDoc(Post post) {
      firestore.collection("Likes")
              .whereEqualTo("postUrl", post.getUrl())
              .whereEqualTo("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail())
              .get()
              .addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       // Belge bulundu, belge kimliğini al
                       String documentId = document.getId();
                       // Belgeyi sil
                       firestore.collection("Likes").document(documentId).delete();
                    }
                 }
              });
   }

   private void createLikesDoc(Post post) {
      HashMap<String, Object> map = new HashMap<>();
      map.put("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail());
      map.put("postUrl", post.getUrl());
      firestore.collection("Likes").add(map);
   }

   private void changePostLike(Post post, int value) {
      firestore.collection("Posts")
              .whereEqualTo("url", post.getUrl()).get()
              .addOnCompleteListener(task -> {
                 if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                       // Belge bulununca belge kimliğini al.
                       String documentId = document.getId();

                       // Belge kimliği kullanarak likeCount değerini değiştir.
                       firestore.collection("Posts")
                               .document(documentId)
                               .update("likeCount", FieldValue.increment(value));
                    }
                 }
              });
   }

   private CompletableFuture<Boolean> hasPostLiked(Post post) {
      CompletableFuture<Boolean> future = new CompletableFuture<>();

      firestore.collection("Likes")
              .whereEqualTo("postUrl", post.getUrl()) //Beğenilen post
              .whereEqualTo("email", Objects.requireNonNull(auth.getCurrentUser()).getEmail()) //Beğenen kişi
              .get()
              .addOnCompleteListener(task -> {
                 if (task.getResult().getDocuments().isEmpty()) {
                    future.complete(true);
                 }
                 else {
                    future.complete(false);
                 }
              });

      return future;
   }
}