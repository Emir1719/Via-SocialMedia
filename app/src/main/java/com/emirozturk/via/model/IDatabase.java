package com.emirozturk.via.model;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface IDatabase {
   void uploadPost(Uri selectedImageUri, String comment);
   CompletableFuture<ArrayList<Post>> getPosts(FirebaseUser user);
   CompletableFuture<ArrayList<Post>> getAllPost();
   CompletableFuture<Boolean> deletePost(Post post);
}