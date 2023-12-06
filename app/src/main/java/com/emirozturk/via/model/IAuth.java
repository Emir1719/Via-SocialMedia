package com.emirozturk.via.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

public interface IAuth {
   boolean register(String email, String password);
   boolean login(String email, String password);
   boolean signOut();
   boolean isHaveUser();
   FirebaseUser currentUser();
}