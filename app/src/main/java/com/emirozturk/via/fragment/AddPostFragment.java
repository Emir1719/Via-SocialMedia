package com.emirozturk.via.fragment;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.emirozturk.via.R;
import com.emirozturk.via.databinding.FragmentAddPostBinding;
import com.emirozturk.via.model.IDatabase;
import com.emirozturk.via.service.FirebaseDB;
import com.emirozturk.via.widget.AppMessage;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;

public class AddPostFragment extends Fragment {
   private FragmentAddPostBinding binding;
   private ActivityResultLauncher<Intent> activityResultLauncher;
   private ActivityResultLauncher<String> permissionLauncher;
   private Uri selectedImageUri;
   private IDatabase database;

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
      database = new FirebaseDB(requireContext());
   }

   public void upload(View view) {
      try {
         if (binding.imageUserPost.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.image)).getConstantState()) {
            //Bir görsel seçilmediyse yüklemeyi iptal et.
            AppMessage.show(requireContext(), "Lütfen bir resim seçiniz!");
            return;
         }
         if (Objects.requireNonNull(binding.userComment.getText()).toString().isEmpty()) {
            AppMessage.show(requireContext(), "Lütfen bir yorum yazınız!");
            return;
         }

         database.uploadPost(selectedImageUri, binding.userComment.getText().toString());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void select(View view) {
      try {
         // API 18 ve öncesi için izin almaya gerek yoktur.
         // API seviyesini tespit eden kod.
         String permission;
         if (Build.VERSION.SDK_INT >= 33) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
         } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
         }

         if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            //İzin verilmediyse izin istenir:
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
               Snackbar.make(view, "Galerinize gitmek için izin gerekli.", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", view1 -> {
                  //request permission
                  permissionLauncher.launch(permission);
               }).show();
            }
            else {
               //request permission
               permissionLauncher.launch(permission);
            }
         }
         else {
            //İzin verilmişse galeriye gidilir.
            gotoGallery();
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void registerLauncher() {
      try {
         //ActivityResultContracts.StartActivityForResult(): bir sonuç için yeni bir aktivite başlatacağız.
         activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
               Intent intent = result.getData();
               if (intent != null) {
                  //verinin kayıtlı olduğu lokasyonu verir.
                  selectedImageUri = intent.getData();
                  binding.imageUserPost.setImageURI(selectedImageUri);
               }
            }
         });

         permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
               //Permission granted
               gotoGallery();
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

   private void gotoGallery() {
      Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      activityResultLauncher.launch(intent);
   }
}