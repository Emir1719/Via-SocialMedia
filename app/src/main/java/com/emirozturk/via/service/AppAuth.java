package com.emirozturk.via.service;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.emirozturk.via.view.MainActivity;
import com.emirozturk.via.widget.AppMessage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

public class AppAuth implements IAuth {
   private final FirebaseAuth auth;
   private final Context context;

   public AppAuth(Context context) {
      auth = FirebaseAuth.getInstance();
      this.context = context;
   }

   @Override
   public boolean register(String email, String password) {
      AtomicBoolean result = new AtomicBoolean(false);
      try {
         if (!email.isEmpty() && !password.isEmpty()) {
            /*auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
               @Override
               public void onSuccess(AuthResult authResult) {
                  gotoMainActivity();
               }
            });*/
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               gotoMainActivity();
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

   private void gotoMainActivity() {
      Intent intent = new Intent(context, MainActivity.class);
      context.startActivity(intent);
   }
}