package com.example.uniorproject.rest;

import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.User;

public interface AppAPI {
    void fillUser();
    void fillRecipe();
    void fillPost();

    void fillPicture();

    void addUser(User user);
    void addRecipe(Recipe recipe);
    void addPost(Post post);

    void addPicture(Picture picture);

    void deleteRecipe(int id);
}
