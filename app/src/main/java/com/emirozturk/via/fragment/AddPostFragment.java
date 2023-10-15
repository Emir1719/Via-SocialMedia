package com.emirozturk.via.fragment;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirozturk.via.databinding.FragmentAddPostBinding;
import com.emirozturk.via.widget.AppMessage;
import com.google.android.material.snackbar.Snackbar;

public class AddPostFragment extends Fragment {
   FragmentAddPostBinding binding;
   ActivityResultLauncher<Intent> activityResultLauncher;
   ActivityResultLauncher<String> permissionLauncher;
   Bitmap selectedImage;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      binding = FragmentAddPostBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      registerLauncher();
      binding.imageUserPost.setOnClickListener(this::select);
      binding.upload.setOnClickListener(this::upload);
   }

   public void upload(View view) {
      try {
         System.out.println("Gelecek...");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void select(View view) {
      try {
         // API 18 ve öncesi için izin almaya gerek yoktur.
         // API seviyesini tespit eden ContextCompat kodu kullanıldı.
         String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
         if (Build.VERSION.SDK_INT >= 33) {
            permission =  Manifest.permission.READ_MEDIA_IMAGES;
         }

         if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            //İzin verilmediyse izin istenir:
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
               String finalPermission = permission; // onClick'te erişebilmem için yeni değişkene atandı.
               Snackbar.make(view, "Galerinize gitmek için izin gerekli.", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", view1 -> {
                  //request permission
                  permissionLauncher.launch(finalPermission);
               }).show();
            }
            else {
               //request permission
               permissionLauncher.launch(permission);
            }
         }
         else {
            //İzin verilmişse galeriye gidilir.
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void registerLauncher() {
      try {
         activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
               Intent intent = result.getData();
               if (intent != null) {
                  Uri imageData = intent.getData();
                  try {
                     if (Build.VERSION.SDK_INT >= 28) {
                        if (imageData == null) {
                           AppMessage.show(getContext(), "Lütfen resim yükleyin!");
                           return;
                        }
                        ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(),imageData);
                        selectedImage = ImageDecoder.decodeBitmap(source);
                     }
                     else {
                        selectedImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),imageData);
                     }
                     binding.imageUserPost.setImageBitmap(selectedImage);
                  }
                  catch (Exception e) {
                     e.printStackTrace();
                  }
               }
            }
         });

         permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
               //Permission granted
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               activityResultLauncher.launch(intent);
            }
            else {
               //Permission denied
               AppMessage.show(requireContext(), "İzin gerekli!");
            }
         });
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}