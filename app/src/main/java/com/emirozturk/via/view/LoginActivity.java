package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.emirozturk.via.databinding.ActivityLoginBinding;
import com.emirozturk.via.service.AppAuth;
import com.emirozturk.via.model.IAuth;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
   private ActivityLoginBinding binding;
   private String email, password;
   private IAuth auth;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityLoginBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      auth = new AppAuth(this);
   }

   @Override
   protected void onStart() {
      super.onStart();
      if (auth.isHaveUser()) {
         //Kullanıcı varsa ana sayfaya geç.
         gotoActivity();
      }
   }

   private void getEmailAndPassword() {
      email = Objects.requireNonNull(binding.editMail.getText()).toString();
      password = Objects.requireNonNull(binding.editPassword.getText()).toString();
   }

   public void login(View view) {
      getEmailAndPassword();
      auth.login(email, password).thenAccept(result -> {
         gotoActivity();
      });
   }

   public void register(View view) {
      getEmailAndPassword();
      auth.register(email, password).thenAccept(result -> {
         gotoActivity();
      });
   }

   private void gotoActivity() {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
   }
}