package com.lekai.root.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lekai.root.bakingapp.R;
import com.lekai.root.bakingapp.Recipes.Step;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by root on 6/6/17.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewholder> {
    Context mContext;
    ArrayList<Step> mySteps ;
    ItemToucher mCallback;

    public StepAdapter(Context context, ArrayList<Step> steps){
        mContext = context;
        mySteps = steps;
    }
    @Override
    public StepAdapter.StepAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.each_step_view,parent,false);
        return new StepAdapterViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(final StepAdapter.StepAdapterViewholder holder, final int position) {
        final String step = mySteps.get(position).getShortDescription();
        final String thumbnailUrl = mySteps.get(position).getThumbnailURL();
        holder.stepTextView.setText(step);
        final List<Step> myStep = new ArrayList<>();
        try {
            mCallback = (ItemToucher) mContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public int getItemCount() {
        return mySteps.size();
    }

    public class StepAdapterViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(R.id.step_textview)
        TextView stepTextView;
        public StepAdapterViewholder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCallback.onItemTouch(position);
        }
    }
    public interface ItemToucher {
        void onItemTouch(int position);
    }
}
