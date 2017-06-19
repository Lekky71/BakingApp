package com.lekai.root.bakingapp.Endpoint;

import com.lekai.root.bakingapp.Recipes.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by root on 6/5/17.
 */

public interface RecipeEndpointInterface {
    @GET("May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
