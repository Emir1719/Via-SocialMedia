package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emirozturk.via.R;

public class LoginActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);
   }

   public void login(View view) {
      try {
         Intent intent = new Intent(this, MainActivity.class);
         startActivity(intent);
         finish();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void register(View view) {
      try {

      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}