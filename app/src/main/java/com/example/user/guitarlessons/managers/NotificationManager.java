package com.example.user.guitarlessons.managers;

import com.backendless.Backendless;
import com.backendless.DeviceRegistration;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by user on 16.03.2018.
 */

public class NotificationManager {

    private static NotificationManager instance;

     public static NotificationManager getInstance() {

        if (instance==null){
            instance=new NotificationManager();
        }
        return instance;
    }
    private static String SENDER_ID="964645203843";

    public void getDeviceRegistration(final NotificationListener listener) {
        Backendless.Messaging.getDeviceRegistration(new AsyncCallback<DeviceRegistration>() {
            @Override
            public void handleResponse(DeviceRegistration response) {
               if (listener!=null){
                   listener.onSuccess();
               }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                registerDevice(listener);
            }
        });
    }
    private void registerDevice(final NotificationListener listener){
        Backendless.Messaging.registerDevice(SENDER_ID, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                if (listener!=null){
                    listener.onSuccess();
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener!=null){
                    listener.onError(fault);
                }
            }
        });
    }
    public interface NotificationListener{
        void onSuccess();
        void onError(BackendlessFault fault);
    }
}
