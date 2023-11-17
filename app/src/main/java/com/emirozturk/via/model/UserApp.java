package com.emirozturk.via.model;
import java.util.ArrayList;

public class UserApp {
   private String id, email, name, profilUrl;
   private ArrayList<Post> posts;

   public UserApp() {}

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getProfilUrl() {
      return profilUrl;
   }

   public void setProfilUrl(String profilUrl) {
      this.profilUrl = profilUrl;
   }

   public ArrayList<Post> getPosts() {
      return posts;
   }

   public void setPosts(ArrayList<Post> posts) {
      this.posts = posts;
   }
}