package com.lekai.root.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lekai.root.bakingapp.R;
import com.lekai.root.bakingapp.Recipes.Ingredient;
import com.lekai.root.bakingapp.Recipes.Recipe;
import com.lekai.root.bakingapp.Recipes.Step;
import com.lekai.root.bakingapp.StepsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by root on 6/5/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewholder>{
    ArrayList<Recipe> mRecipes;
    Context mContext;
    OnItemLongClickListener mCallback;
    public RecipeAdapter(Context context,ArrayList<Recipe> recipes){
        mContext = context;
        mRecipes = recipes;
    }
    @Override
    public RecipeAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_recipe_view,parent,false);
        return new RecipeAdapterViewholder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeAdapterViewholder holder, final int position) {
        String imageUrl = mRecipes.get(position).getImage();
        if(imageUrl != "") {
            Picasso.with(mContext).load(imageUrl)
                    .fit()
                    .placeholder(R.color.colorBlack)
                    .into(holder.recipeImage);
        }else {
            holder.recipeImage.setImageResource(R.mipmap.no_image_pic);
        }
            String name = mRecipes.get(position).getName();
            holder.recipeNameTextView.setText(name);
        holder.recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StepsActivity.class);
                intent.putParcelableArrayListExtra("ingredients",(ArrayList<Ingredient>)mRecipes.get(position).getIngredients());
                intent.putParcelableArrayListExtra("steps",(ArrayList<Step>)mRecipes.get(position).getSteps());
                mContext.startActivity(intent);
            }
        });
        try {
            mCallback = (OnItemLongClickListener) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeAdapterViewholder extends RecyclerView.ViewHolder{
        @InjectView(R.id.recipe_imageview)
        ImageView recipeImage;
        @InjectView(R.id.recipe_name)
        TextView recipeNameTextView;
        public RecipeAdapterViewholder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
    public interface OnItemLongClickListener {
        void onItemLongClicked(int position);
    }
}
