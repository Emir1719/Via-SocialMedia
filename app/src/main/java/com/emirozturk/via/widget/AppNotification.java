package com.emirozturk.via.widget;
import android.content.Context;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import java.util.concurrent.CompletableFuture;

public class AppNotification {
   private static final String ONESIGNAL_APP_ID = "3679a38f-81ea-4be3-b8d6-4ee76be42ab5";
   private Context context;

   public AppNotification(Context context) {
      this.context = context;
   }

   public CompletableFuture<Boolean> init() {
      CompletableFuture<Boolean> future = new CompletableFuture<>();
      // Verbose Logging set to help debug issues, remove before releasing your app.
      OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

      // OneSignal Initialization
      OneSignal.initWithContext(context, ONESIGNAL_APP_ID);

      // requestPermission will show the native Android notification permission prompt.
      // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
      OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
         if (!r.isSuccess()) {
            future.complete(false);
         }
      }));
      future.complete(true);
      return future;
   }

   public void send() {

   }

   //playerID'yi döndürür.
   public String getID() {
      return OneSignal.getUser().getPushSubscription().getId();
   }
}