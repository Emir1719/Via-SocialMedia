package com.emirozturk.via.model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
   private String email, comment, url;
   private int likeCount;

   public Post() {}

   public Post(String email, String comment, String url) {
      setEmail(email);
      setComment(comment);
      setUrl(url);
      setLikeCount(0);
   }

   public void fromMap(Map<String, Object> map) {
      setEmail((String) map.get("email"));
      setUrl((String) map.get("url"));
      setComment((String) map.get("comment"));
      // Firestore'dan gelen sayısal değeri Long türünde al
      Long likeCountLong = (Long) map.get("likeCount");

      // Long türündeki değeri Integer türüne dönüştür
      if (likeCountLong != null) {
         setLikeCount(likeCountLong.intValue());
      } else {
         setLikeCount(0);
      }
   }

   public Map<String, Object> toMap() {
      HashMap<String, Object> map = new HashMap<>();
      map.put("email", getEmail());
      map.put("url", getUrl());
      map.put("comment", getComment());
      map.put("likeCount", getLikeCount());
      return map;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public int getLikeCount() {
      return likeCount;
   }

   public void setLikeCount(int likeCount) {
      this.likeCount = likeCount;
   }
}