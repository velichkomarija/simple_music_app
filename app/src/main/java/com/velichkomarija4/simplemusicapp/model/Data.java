package com.velichkomarija4.simplemusicapp.model;

import com.google.gson.annotations.SerializedName;

public class Data<T> {
    @SerializedName("data")
    public T response;
}
