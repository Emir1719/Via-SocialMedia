package com.emirozturk.via.service;
import android.content.Context;

import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

public class AppAuth implements IAuth {
   private FirebaseAuth auth;
   private Context context;

   public AppAuth(Context context) {
      auth = FirebaseAuth.getInstance();
      this.context = context;
   }

   @Override
   public boolean register(String email, String password) {
      AtomicBoolean result = new AtomicBoolean(false);
      try {
         if (!email.isEmpty() && !password.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
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
      return false;
   }

   @Override
   public boolean signOut() {
      return false;
   }
}