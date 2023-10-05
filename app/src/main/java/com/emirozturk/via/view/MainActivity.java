package com.emirozturk.via.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.emirozturk.via.R;
import com.emirozturk.via.databinding.ActivityMainBinding;
import com.emirozturk.via.fragment.AddPostFragment;
import com.emirozturk.via.fragment.HomeFragment;
import com.emirozturk.via.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
   private ActivityMainBinding binding;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      replaceFragment(new HomeFragment());
      setupBottomNavbar();
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