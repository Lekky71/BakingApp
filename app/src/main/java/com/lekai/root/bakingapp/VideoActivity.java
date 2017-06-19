package com.lekai.root.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lekai.root.bakingapp.ExtraUtil.Constants;
import com.lekai.root.bakingapp.Recipes.Step;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VideoActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private String recipe_name;
    private Step step;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean landScape;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landScape =true;
            addFullScreenParameters();
        }else{
            landScape =false;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.inject(this);
        if(getIntent() == null){
            System.out.println("I have received it oooooo");
        }
            Bundle receivedBundle = getIntent().getBundleExtra("step");
            step = receivedBundle.getParcelable("testing");
            Log.d("it saw it",step.getVideoURL().toString());

        if (!landScape) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(recipe_name);
        }else {
            toolbar.setVisibility(View.GONE);
        }
        showDetail();
    }

    private void addFullScreenParameters(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showDetail() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(Constants.STEP_EXTRA,step);

        if (getSupportFragmentManager().findFragmentByTag(Constants.STEP_DETAIL_TAG)==null) {

            VideoActivityFragment recipeStepDetailFragment = new VideoActivityFragment();
            recipeStepDetailFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_container, recipeStepDetailFragment,Constants.STEP_DETAIL_TAG);
            transaction.commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
