package com.emirozturk.via.model;
import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public interface IDatabase {
   void uploadPost(Uri selectedImageUri, String comment);
   CompletableFuture<ArrayList<Post>> getPosts(FirebaseUser user);
   CompletableFuture<ArrayList<Post>> getAllPost();
   CompletableFuture<Boolean> deletePost(Post post);
   CompletableFuture<Boolean> likePost(Post post);
   CompletableFuture<String> getLikeCount(Post post);
   CompletableFuture<ArrayList<Comment>> getAllComment(Post post);
   CompletableFuture<Boolean> saveComment(Comment comment, Post post);
}