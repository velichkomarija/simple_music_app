package com.velichkomarija4.simplemusicapp;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.velichkomarija4.simplemusicapp.model.DataConverterFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static final String SERVER_URL = "https://android.academy.e-legion.com/api/";

    public static final List<Class<?>> NETWORK_EXCEPTIONS = Arrays.asList(
            UnknownHostException.class,
            SocketTimeoutException.class,
            ConnectException.class
    );

    public static final short SUCCESS_200 = 200;
    public static final short SUCCESS_204 = 204;
    public static final short ERROR_400 = 400;
    public static final short ERROR_401 = 401;
    public static final short ERROR_404 = 404;
    public static final short ERROR_500 = 500;

    private static OkHttpClient client;
    private static Retrofit retrofit;
    private static Gson sGson;
    private static AcademyApi api;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    private static Retrofit getRetrofit() {
        if (sGson == null) {
            sGson = new GsonBuilder()
                    .setDateFormat("dd.MM.yyyy HH:mm:ss")
                    .create();
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .client(getBasicAuthClient("", "", false))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(sGson))
                    .addConverterFactory(new DataConverterFactory())
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    private static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                Retrofit.Builder builder =
                        new Retrofit.Builder()
                                .baseUrl(SERVER_URL)
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .addConverterFactory(new DataConverterFactory())
                                .addConverterFactory(GsonConverterFactory.create());
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }


    private static OkHttpClient getBasicAuthClient(final String email,
                                                   final String password,
                                                   boolean createNewInstance) {
        if (createNewInstance || client == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

            builder.authenticator((route, response) -> {
                String credential = Credentials.basic(email, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            });

            if (!BuildConfig.BUILD_TYPE.contains("release")) {
                builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            client = builder.build();
        }
        return client;
    }

    public static AcademyApi getApi() {
        if (api == null) {
            api = getRetrofit().create(AcademyApi.class);
        }
        return api;
    }
}
