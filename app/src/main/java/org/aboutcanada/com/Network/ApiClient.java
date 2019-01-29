package org.aboutcanada.com.Network;

import android.content.Context;

import org.aboutcanada.com.Ui.MainActivity;
import org.aboutcanada.com.Util.URL;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.aboutcanada.com.Util.URL.MAIN_URL;

public class ApiClient {

    public static final String BASE_URL = URL.MAIN_URL;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context mContext) {

        if (retrofit==null) {

            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(mContext.getCacheDir(), cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
