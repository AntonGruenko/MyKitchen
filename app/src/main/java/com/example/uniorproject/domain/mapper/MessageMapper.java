package com.example.uniorproject.domain.mapper;

import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.User;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageMapper {

    public static Message messageFromJson(JSONObject jsonObject)  {
        Message message = null;

        try {
            message = new Message(
                    jsonObject.getInt("id"),
                    UserMapper.userFromSenderJson(jsonObject),
                    UserMapper.userFromReceiverJson(jsonObject),
                    jsonObject.getString("text"),
                    jsonObject.getString("picture"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
}
