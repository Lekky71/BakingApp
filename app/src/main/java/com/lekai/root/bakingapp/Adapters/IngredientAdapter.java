package com.lekai.root.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lekai.root.bakingapp.R;
import com.lekai.root.bakingapp.Recipes.Ingredient;

import java.util.ArrayList;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewholder> {
    Context mContext;
    ArrayList<Ingredient> myIngredients ;
    public IngredientAdapter(Context context,ArrayList<Ingredient> ingredients) {
        mContext = context;
        myIngredients = ingredients;
    }

    @Override
    public IngredientAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.each_ingredient_view,parent,false);
        return new IngredientAdapterViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientAdapterViewholder holder, int position) {
        Ingredient ingredient = myIngredients.get(position);
        String amount = ingredient.getQuantity().toString();
        String measure = ingredient.getMeasure();
        String material = ingredient.getIngredient();
        String oneIngredients = amount +" "+ measure +" "+ material;
        holder.ingredrientTextView.setText(oneIngredients);
    }

    @Override
    public int getItemCount() {
        return myIngredients.size();
    }

    public class IngredientAdapterViewholder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ingredient_text_view)
        TextView ingredrientTextView;
        public IngredientAdapterViewholder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
