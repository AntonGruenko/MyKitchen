package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Subscription;
import com.example.uniorproject.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public class SubscriptionMapper {

    public static Subscription subscriptionFromJson(JSONObject jsonObject){
        Subscription subscription = null;

        try {
            subscription = new Subscription(
                    jsonObject.getInt("id"),
                    UserMapper.userFromLeaderJson(jsonObject),
                    UserMapper.userFromFollowerJson(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return subscription;
    }
}
