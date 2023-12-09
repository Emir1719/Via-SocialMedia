package com.emirozturk.via.model;
import com.google.firebase.auth.FirebaseUser;
import java.util.concurrent.CompletableFuture;

public interface IAuth {
   CompletableFuture<Boolean> register(String email, String password);
   CompletableFuture<Boolean> login(String email, String password);
   CompletableFuture<Boolean> signOut();
   boolean isHaveUser();
   FirebaseUser currentUser();
}