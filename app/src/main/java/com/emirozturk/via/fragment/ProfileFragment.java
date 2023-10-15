package com.emirozturk.via.fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirozturk.via.R;
import com.emirozturk.via.databinding.FragmentAddPostBinding;
import com.emirozturk.via.databinding.FragmentProfileBinding;
import com.emirozturk.via.view.LoginActivity;
import com.emirozturk.via.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfileFragment extends Fragment {
   private FragmentProfileBinding binding;
   private FirebaseAuth auth;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentProfileBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      binding.btnSignOut.setOnClickListener(this::signOut);
      auth = FirebaseAuth.getInstance();
   }

   public void signOut(View view) {
      auth.signOut();
      Intent intent = new Intent(requireActivity(), LoginActivity.class);
      startActivity(intent);
      requireActivity().finish(); //framentın bağlı olduğu activity'i sonlandırır ki geri dönülmesin
   }
}