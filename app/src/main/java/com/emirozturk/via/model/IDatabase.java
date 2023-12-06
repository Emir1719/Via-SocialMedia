package com.emirozturk.via.model;
import android.net.Uri;
import com.google.firebase.firestore.auth.User;
import java.util.ArrayList;

public interface IDatabase {
   void uploadPost(Uri selectedImageUri, String comment);
   ArrayList<Post> getPosts(User user);
   ArrayList<Post> getAllPost();
   //void deletePost();
}