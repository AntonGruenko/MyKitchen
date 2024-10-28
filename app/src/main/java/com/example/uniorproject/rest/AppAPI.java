package com.example.uniorproject.rest;

import com.example.uniorproject.adapter.RecipeSearchAdapter;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.PostLike;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.RecipeComment;
import com.example.uniorproject.domain.RecipeLike;
import com.example.uniorproject.domain.Subscription;
import com.example.uniorproject.domain.User;

import java.util.List;

public interface AppAPI {
    void fillUser();

    void findUserById(int id, VolleyCallback callback);

    void findUsersByName(String name, VolleyCallback callback);

    void fillRecipe(VolleyCallback callback);

    void findRecipeById(int id, VolleyCallback callback);

    void findRecipesByAuthor(int authorId, VolleyCallback callback);

    void findRecipesByAuthors(List<Integer> authors, VolleyCallback callback);

    void findRecipesByComplexity(int complexity, VolleyCallback callback);

    void findRecipesByKcalBetween(int kcalBottom, int kcalTop, VolleyCallback callback);

    void findRecipesByProteinsBetween(int proteinsBottom, int proteinsTop, VolleyCallback callback);

    void findRecipesByFatsBetween(int fatsBottom, int fatsTop, VolleyCallback callback);

    void findRecipesByCarbohydratesBetween(int carbohydratesBottom, int carbohydratesTop, VolleyCallback callback);

    void findRecipesByTimeBetween(int timeBottom, int timeTop, VolleyCallback callback);

    void findRecipesByIngredients(List<String> ingredients, VolleyCallback callback);

    void findRecipesByIngredientsNot(List<String> ingredients, VolleyCallback callback);

    void findRecipesByTags(List<String> tags, VolleyCallback callback);

    void findRecipesByTagsNot(List<String> tags, VolleyCallback callback);

    void findUserByEmail(String email, VolleyCallback callback);

    void fillPost(int postType, VolleyCallback callback);

    void findPostByAuthor(int authorId, VolleyCallback callback);

    void fillPicture();

    void fillMeals();

    void findMealsByUser(int userId, VolleyCallback callback);

    void findMealsByUserAndDay(int userId, int dayId, VolleyCallback callback);

    void fillDays();

    void findDaysByUserAndDay(int userId, int day, VolleyCallback callback);

    void findDaysByUser(int userId, VolleyCallback callback);

    void findDayById(int id, VolleyCallback callback);

    void findPreviews(VolleyCallback callback);

    void findRecipeCommentsByRecipe(int recipeId, VolleyCallback callback);

    void findSubscriptionsByLeaderId(int leaderId, VolleyCallback callback);

    void findSubscriptionByFollowerId(int followerId, VolleyCallback callback);

    void checkSubscription(int leaderId, int followerId, VolleyCallback callback);

    void findMessagesBySenderAndReceiver(int senderId, int receiverId, VolleyCallback callback);

    void findMessagesByUser(int userId, VolleyCallback callback);

    void addUser(User user);

    void updateUser(int id,
                    String name,
                    String email,
                    String password,
                    String status,
                    String profilePic,
                    int kcal,
                    int proteins,
                    int fats,
                    int carbohydrates,
                    long registrationDate);

    void updateUser(int id,
                    String name,
                    String email,
                    String password,
                    String status,
                    String profilePic,
                    int kcal,
                    int proteins,
                    int fats,
                    int carbohydrates,
                    long registrationDate,
                    VolleyCallback callback);

    void addRecipe(Recipe recipe, VolleyCallback callback);

    void addPost(Post post, VolleyCallback callback);

    void addPicture(Picture picture);

    void addMeal(Meal meal);

    void addRecipeLike(RecipeLike recipeLike, VolleyCallback callback);

    void addPostLike(PostLike postLike, VolleyCallback callback);

    void deleteMeal(Meal meal, VolleyCallback callback);

    void addDay(Day day, VolleyCallback callback);

    void updateDay(int id,
                   int userId,
                   int day,
                   int kcal,
                   int proteins,
                   int fats,
                   int carbohydrates,
                   boolean isSuccessful,
                   VolleyCallback callback);

    void addRecipeComment(RecipeComment recipeComment, VolleyCallback callback);

    void addSubscription(Subscription subscription, VolleyCallback callback);

    void addMessage(Message message, VolleyCallback callback);

    void findRecipeLikesByRecipe(int recipeId, VolleyCallback callback);

    void findPostLikesByPost(int postId, VolleyCallback callback);

    void deleteRecipe(int id);

    void deleteRecipeLike(Recipe recipe, User user, VolleyCallback callback);

    void deletePostLike(Post post, User user, VolleyCallback callback);

    void deleteSubscription(int leaderId, int followerId, VolleyCallback callback);


}
