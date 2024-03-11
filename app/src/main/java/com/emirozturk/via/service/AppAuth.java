package com.emirozturk.via.service;
import android.content.Context;
import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.concurrent.CompletableFuture;

public class AppAuth implements IAuth {
   private final FirebaseAuth auth;
   private final Context context;
   private FirebaseUser currentUser;

   public AppAuth(Context context) {
      auth = FirebaseAuth.getInstance();
      this.context = context;
      this.currentUser = auth.getCurrentUser();
   }

   @Override
   public CompletableFuture<Boolean> register(String email, String password) {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      try {
         if (!email.isEmpty() && !password.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               future.complete(true);
            }).addOnFailureListener(e -> {
               AppMessage.show(context, e.getLocalizedMessage());
            });
         }
         else {
            AppMessage.show(context, "Email veya şifre alanı boş olamaz!");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return future;
   }

   @Override
   public CompletableFuture<Boolean> login(String email, String password) {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      try {
         if (!email.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               future.complete(true);
            }).addOnFailureListener(e -> {
               AppMessage.show(context, e.getLocalizedMessage());
            });
         }
         else {
            AppMessage.show(context, "Email veya şifre alanı boş olamaz!");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return future;
   }

   @Override
   public CompletableFuture<Boolean> signOut() {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      try {
         auth.signOut();
         future.complete(true);
      }
      catch (Exception e) {
         future.completeExceptionally(e);
      }
      return future;
   }

   @Override
   public boolean isHaveUser() {
      return currentUser != null;
   }

   @Override
   public FirebaseUser currentUser() {
      return auth.getCurrentUser();
   }
}