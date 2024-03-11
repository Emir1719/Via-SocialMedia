package com.emirozturk.via.model;
import java.util.HashMap;
import java.util.Map;

public class AppUser {
   private String id, email, profileUrl;

   public void fromMap(Map<String, Object> map) {
      setId((String) map.get("id"));
      setEmail((String) map.get("email"));
      setProfileUrl((String) map.get("profileUrl"));
   }

   public Map<String, Object> toMap() {
      Map<String, Object> map = new HashMap<>();
      map.put("id", getId());
      map.put("email", getEmail());
      map.put("profileUrl", getProfileUrl());
      return map;
   }

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

   public String getProfileUrl() {
      return profileUrl;
   }

   public void setProfileUrl(String profileUrl) {
      this.profileUrl = profileUrl;
   }
}