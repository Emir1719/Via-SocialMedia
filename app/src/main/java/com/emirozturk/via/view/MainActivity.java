package com.emirozturk.via.view;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.emirozturk.via.R;
import com.emirozturk.via.databinding.ActivityMainBinding;
import com.emirozturk.via.fragment.AddPostFragment;
import com.emirozturk.via.fragment.HomeFragment;
import com.emirozturk.via.fragment.ProfileFragment;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.service.AppAuth;
import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.service.FirebaseDB;
import com.emirozturk.via.widget.AppNotification;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.common.OneSignalUtils;
import com.onesignal.debug.LogLevel;

public class MainActivity extends AppCompatActivity {
   private ActivityMainBinding binding;
   private IAuth auth;
   private IDatabase database;
   private AppNotification notification;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      replaceFragment(new HomeFragment());
      setupBottomNavbar();
      auth = new AppAuth(this);
      database = new FirebaseDB(this);
      notification = new AppNotification(this);
      notification.init().thenAccept(aBoolean -> {
         if (aBoolean) {
            database.saveUser();
         }
      });

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.exit_button, menu);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      try {
         if (item.getItemId() == R.id.exit) {
            auth.signOut().thenAccept(aBoolean -> {
               Intent intent = new Intent(this, LoginActivity.class);
               startActivity(intent);
               finish();
            });
         }
         return true;
      }
      catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private void setupBottomNavbar() {
      binding.bottomNavigationView.setOnItemSelectedListener(item -> {
         int id = item.getItemId();
         if (id == R.id.navigation_home) {
            replaceFragment(new HomeFragment());
         }
         else if (id == R.id.navigation_dashboard) {
            replaceFragment(new AddPostFragment());
         }
         else if (id == R.id.navigation_profile) {
            replaceFragment(new ProfileFragment());
         }
         return true;
      });
   }

   private void replaceFragment(Fragment fragment) {
      try {
         FragmentManager fragmentManager = getSupportFragmentManager();
         FragmentTransaction transaction = fragmentManager.beginTransaction();
         transaction.replace(R.id.frameContainer, fragment);
         transaction.commit();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}