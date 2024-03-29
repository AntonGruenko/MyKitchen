package com.example.uniorproject.rest;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.example.uniorproject.domain.User;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject response);
    void onError(@Nullable VolleyError error);

}
