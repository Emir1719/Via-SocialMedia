package com.emirozturk.via.model;
import java.util.Map;

public class Post {
   private String email, comment, url, likeCount;

   public Post() {}

   public Post(String email, String comment, String url) {
      setEmail(email);
      setComment(comment);
      setUrl(url);
   }

   public void mapToPost(Map<String, Object> map) {
      setEmail((String) map.get("email"));
      setUrl((String) map.get("url"));
      setComment((String) map.get("comment"));
      //setLikeCount((String) map.get("likeCount"));
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

   public String getLikeCount() {
      return likeCount;
   }

   public void setLikeCount(String likeCount) {
      this.likeCount = likeCount;
   }
}