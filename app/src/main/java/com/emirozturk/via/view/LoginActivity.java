package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emirozturk.via.databinding.ActivityLoginBinding;
import com.emirozturk.via.widget.AppMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
   private ActivityLoginBinding binding;
   private FirebaseAuth auth;
   private String email, password;
   //private IAuth appAuth;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityLoginBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      auth = FirebaseAuth.getInstance();
      //appAuth = new AppAuth(this);
   }

   @Override
   protected void onStart() {
      super.onStart();
      FirebaseUser currentUser = auth.getCurrentUser();
      if (currentUser != null){
         //Kullanıcı varsa ana sayfaya geç.
         gotoMainActivity();
      }
   }

   public void login(View view) {
      try {
         email = Objects.requireNonNull(binding.editMail.getText()).toString();
         password = Objects.requireNonNull(binding.editPassword.getText()).toString();

         if (!email.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               gotoMainActivity();
            }).addOnFailureListener(e -> {
               AppMessage.show(this, e.getLocalizedMessage());
            });
         }
         else {
            AppMessage.show(this, "Email veya şifre alanı boş olamaz!");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void register(View view) {
      try {
         email = Objects.requireNonNull(binding.editMail.getText()).toString();
         password = Objects.requireNonNull(binding.editPassword.getText()).toString();

         if (!email.isEmpty() && !password.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
               gotoMainActivity();
            }).addOnFailureListener(e -> {
               AppMessage.show(this, e.getLocalizedMessage());
            });
         }
         else {
            AppMessage.show(this, "Email veya şifre alanı boş olamaz!");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Çalışmalar devam ediyor...
   /*public void registerTry(View view) {
      email = Objects.requireNonNull(binding.editMail.getText()).toString();
      password = Objects.requireNonNull(binding.editPassword.getText()).toString();

      boolean result = appAuth.register(email, password);
      if (result) {
         gotoMainActivity();
      }
   }*/

   private void gotoMainActivity() {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
   }
}