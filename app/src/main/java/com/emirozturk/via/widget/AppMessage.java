package com.emirozturk.via.widget;
import android.content.Context;
import android.widget.Toast;

public class AppMessage {
   public static void show(Context context, String message) {
      Toast.makeText(context, message, Toast.LENGTH_LONG).show();
   }

   public static void showShort(Context context, String message) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
   }
}