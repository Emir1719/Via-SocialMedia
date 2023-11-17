package com.emirozturk.via.service;
import android.content.Context;
import android.content.Intent;

import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.view.LoginActivity;
import com.emirozturk.via.view.MainActivity;
import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicBoolean;

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
   public boolean register(String email, String password) {
      AtomicBoolean result = new AtomicBoolean(false);
      try {
         if (!email.isEmpty() && !password.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               gotoActivity(MainActivity.class);
               result.set(true);
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
      return result.get();
   }

   @Override
   public boolean login(String email, String password) {
      AtomicBoolean result = new AtomicBoolean(false);
      try {
         if (!email.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               gotoActivity(MainActivity.class);
               result.set(true);
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
      return result.get();
   }

   @Override
   public boolean signOut() {
      auth.signOut();
      gotoActivity(LoginActivity.class);
      return false;
   }

   private void gotoActivity(Class<?> cs) {
      Intent intent = new Intent(context, cs);
      context.startActivity(intent);
   }

   public FirebaseUser getCurrentUser() {
      return currentUser;
   }

   @Override
   public boolean isHaveUser() {
      return currentUser != null;
   }
}