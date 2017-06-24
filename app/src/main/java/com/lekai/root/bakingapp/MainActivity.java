package com.lekai.root.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lekai.root.bakingapp.Adapters.RecipeAdapter;
import com.lekai.root.bakingapp.Endpoint.RecipeEndpointInterface;
import com.lekai.root.bakingapp.ExtraUtil.Constants;
import com.lekai.root.bakingapp.ExtraUtil.WorkUtils;
import com.lekai.root.bakingapp.IdlingResource.SimpleIdlingResource;
import com.lekai.root.bakingapp.Recipes.Ingredient;
import com.lekai.root.bakingapp.Recipes.Recipe;
import com.lekai.root.bakingapp.widget.IngredientWidgetProvider;
import com.lekai.root.bakingapp.widget.db.IngredientContract;

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

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemLongClickListener{
    public final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/";
    ArrayList<Recipe> recipes;
    @InjectView(R.id.recipe_recycler_view)
    RecyclerView recyclerView;
    RecipeAdapter recipeAdapter;
    LayoutManager layoutManager;
    Context context;
    int orientation;
    private SharedPreferences sharedPreferences;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (recipes != null && recipes.isEmpty()) {
            makeRequest();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
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
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList("recipe");
        }
        recipeAdapter = new RecipeAdapter(this,recipes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipeAdapter);
        Toast.makeText(context,"Long hold a recipe to add it to the widget",Toast.LENGTH_LONG).show();
        getIdlingResource();
        sharedPreferences=this.getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void makeRequest(){

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        if (WorkUtils.isOnline(this)) {
            getAllRecipes();

        }else{
            Snackbar snackbar = Snackbar.make(recyclerView, R.string.failure_msg, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeRequest();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id ==R.id.add_widget){
            final PopupMenu popupMenu=new PopupMenu(this,recyclerView);
            popupMenu.inflate(R.menu.widget);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_recipe: displayIngredientInWidget(2); break;
                    }
                    return true;
                }
            });
            popupMenu.show();
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
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

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
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(recyclerView, R.string.error_msg, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getAllRecipes();
                    }
                });
                snackbar.show();

            }
        });
    }

    private void displayIngredientInWidget(int adapterPosition) {
        ArrayList<Ingredient> ingredients;
        ingredients=(ArrayList<Ingredient>) recipes.get(adapterPosition).getIngredients();
        String recipe=recipes.get(adapterPosition).getName();
        sharedPreferences.edit().putString(Constants.S_PREF_RECIPE,recipe).apply();


        Uri uri1 = IngredientContract.CONTENT_URI;
        Cursor cursor = this.getContentResolver().query(uri1,null,null,null,null);


        if (cursor!=null) {
            while (cursor.moveToNext()) {
                Uri uri2 = IngredientContract.CONTENT_URI;
                this.getContentResolver().delete(uri2,
                        IngredientContract.Columns._ID + "=?",
                        new String[]{cursor.getString(0)});

            }

            //Insert
            ContentValues values = new ContentValues();

            for (Ingredient ingredient : ingredients) {
                values.clear();
                values.put(IngredientContract.Columns.QUANTITY, ingredient.getQuantity());
                values.put(IngredientContract.Columns.MEASURE, ingredient.getMeasure());
                values.put(IngredientContract.Columns.INGREDIENT, ingredient.getIngredient());


                Uri uri = IngredientContract.CONTENT_URI;
                getApplicationContext().getContentResolver().insert(uri, values);
            }
        }

        //Update the name of the recipe
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IngredientWidgetProvider.class));
        IngredientWidgetProvider ingredientWidget=new IngredientWidgetProvider();
        ingredientWidget.onUpdate(this, AppWidgetManager.getInstance(this),ids);

        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, IngredientWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ing_widget_list);

    }

    @Override
    public void onItemLongClicked(int position) {
        displayIngredientInWidget(position);
    }
}
