package com.emirozturk.via.fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirozturk.via.databinding.FragmentProfileBinding;
import com.emirozturk.via.model.IAuth;
import com.emirozturk.via.service.AppAuth;

public class ProfileFragment extends Fragment {
   private FragmentProfileBinding binding;
   private IAuth auth;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentProfileBinding.inflate(inflater, container, false);
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      auth = new AppAuth(requireContext());
   }
}