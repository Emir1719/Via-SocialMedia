package com.emirozturk.via.model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Comment implements Serializable {
   private String email, message;

   public void fromMap(Map<String, Object> map) {
      setEmail((String) map.get("email"));
      setMessage((String) map.get("message"));
   }

   public Map<String, Object> toMap() {
      Map<String, Object> map = new HashMap<>();
      map.put("email", getEmail());
      map.put("message", getMessage());
      return map;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}