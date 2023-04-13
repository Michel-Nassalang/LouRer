package sn.flexzone.lourer;

import static android.service.controls.ControlsProviderService.TAG;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String actutoken = task.getResult();
                        // Log
                        Log.d(TAG, "Token actuel ---------------------------------------------------------------------------------------: "+actutoken);
                        storeToken(actutoken);
                    }
                });
        Log.d(TAG, "onNewToken: "+ token);
        //storeToken(token);
    }
    private void storeToken(String token ){
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }
}
