package com.example.user.guitarlessons.managers;

import android.content.Context;

import com.example.user.guitarlessons.model.Tuning;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 27.03.2018.
 */

public class TuningsManager {

    private static TuningsManager instance;

    public static TuningsManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new TuningsManager();
        }
    }

    private TuningsManager() {
    }

    public void getTunings(final Context context, final int docId,
                           final TuningsListener listener) {

        Single<String> jsonSingle = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {

                try {
                    InputStream inputStream = context.getResources().openRawResource(docId);
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    emitter.onSuccess(new String(buffer, "UTF-8"));
                } catch (IOException ex) {
                    emitter.onError(ex);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        jsonSingle.subscribeWith(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                Gson gson = new GsonBuilder().create();
                List<Tuning> tunings = gson.fromJson(s, new TypeToken<ArrayList<Tuning>>() {
                }.getType());
                if (listener != null) {
                    listener.onSuccess(tunings);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
        });
    }

    public interface TuningsListener {

        void onSuccess(List<Tuning> tunings);
        void onError(Throwable ex);
    }
}
