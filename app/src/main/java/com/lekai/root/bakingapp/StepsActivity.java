package com.lekai.root.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.lekai.root.bakingapp.Adapters.IngredientAdapter;
import com.lekai.root.bakingapp.Adapters.StepAdapter;
import com.lekai.root.bakingapp.ExtraUtil.Constants;
import com.lekai.root.bakingapp.Recipes.Ingredient;
import com.lekai.root.bakingapp.Recipes.Step;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by root on 6/6/17.
 */

public class StepsActivity extends AppCompatActivity implements StepAdapter.ItemToucher{
    @InjectView(R.id.steps_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.steps_recyclerview)
    RecyclerView stepRecyclerView;
    @InjectView(R.id.ingredient_recyclerview)
    RecyclerView ingredientsRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager1;
    StepAdapter stepAdapter ;
    IngredientAdapter ingredientAdapter;
    Context context;
    ArrayList<Step> steps;
    ArrayList<Ingredient> ingredients;
    int configuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getBaseContext();
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager1 = new LinearLayoutManager(context);
        layoutManager1.setAutoMeasureEnabled(true);
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();
        Intent receivedIntent = getIntent();
        if(receivedIntent.getParcelableArrayListExtra("ingredients") != null){
            steps = receivedIntent.getParcelableArrayListExtra("steps");
            ingredients = receivedIntent.getParcelableArrayListExtra("ingredients");
        }

        if(savedInstanceState != null){
            steps = savedInstanceState.getParcelableArrayList("steps");
            ingredients = savedInstanceState.getParcelableArrayList("ingredients");
        }
        stepAdapter = new StepAdapter(context,steps);
        stepRecyclerView.setAdapter(stepAdapter);
        stepRecyclerView.setLayoutManager(layoutManager);
        ingredientAdapter = new IngredientAdapter(context,ingredients);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        ingredientsRecyclerView.setLayoutManager(layoutManager1);
        configuration = context.getResources().getConfiguration().orientation;
    }

    public void showDetail(int position) {
        Bundle bundle=new Bundle();
        bundle.putParcelable(Constants.STEP_EXTRA,steps.get(position));
        VideoActivityFragment recipeStepDetailFragment = new VideoActivityFragment();
        recipeStepDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_container_land, recipeStepDetailFragment,Constants.STEP_DETAIL_TAG);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Step> steps = this.steps;
        ArrayList<Ingredient> ingredients = this.ingredients;
        outState.putParcelableArrayList("steps",steps);
        outState.putParcelableArrayList("ingredients",ingredients);
    }

    @Override
    public void onItemTouch(int position) {

        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && findViewById(R.id.recipe_container_land) != null) {
            showDetail(position);


        }else{
            Step steper = steps.get(position);
            Intent intent = new Intent(context,VideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("testing", steper);
            intent.putExtra("step",bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
