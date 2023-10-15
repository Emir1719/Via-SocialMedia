package com.emirozturk.via.service;

public interface IAuth {
   boolean register(String email, String password);
   boolean login(String email, String password);
   boolean signOut();
}