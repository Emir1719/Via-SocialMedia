package com.emirozturk.via.model;

public interface IAuth {
   boolean register(String email, String password);
   boolean login(String email, String password);
   boolean signOut();
   boolean isHaveUser();
}