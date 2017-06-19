package com.lekai.root.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lekai.root.bakingapp.Adapters.RecipeAdapter;
import com.lekai.root.bakingapp.Endpoint.RecipeEndpointInterface;
import com.lekai.root.bakingapp.Recipes.Ingredient;
import com.lekai.root.bakingapp.Recipes.Recipe;
import com.lekai.root.bakingapp.Recipes.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v7.widget.RecyclerView.LayoutManager;

public class MainActivity extends AppCompatActivity {
    public final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/";
    ArrayList<Recipe> recipes;
    @InjectView(R.id.recipe_recycler_view)
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    LayoutManager layoutManager;
    Context context;
    int orientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recipes = new ArrayList<>();
        context = getBaseContext();
        orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE || context.getResources().getConfiguration().smallestScreenWidthDp == 600){
            layoutManager = new GridLayoutManager(context,2);
        }
        else{
            layoutManager = new LinearLayoutManager(context);
        }
        layoutManager.setAutoMeasureEnabled(true);
        getAllRecipes();
        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList("recipe");
        }
        recipeAdapter = new RecipeAdapter(this,recipes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipeAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Recipe> myRecipes = recipes;
        outState.putParcelableArrayList("recipe",myRecipes);
    }

    private void getAllRecipes(){

        Retrofit retrofit;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeEndpointInterface apiService = retrofit.create(RecipeEndpointInterface.class);

        Call<List<Recipe>> call = apiService.getRecipes();
        call.enqueue(new Callback <List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes.clear();
                recipes.addAll(response.body());
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                String TAG = "failed to connect";
                Log.e(TAG, t.getMessage());
                Toast.makeText(getBaseContext(), "An error occured, Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
