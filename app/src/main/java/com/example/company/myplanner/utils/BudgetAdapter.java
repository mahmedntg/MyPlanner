package com.example.company.myplanner.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.company.myplanner.PlannerActivity;
import com.example.company.myplanner.R;
import com.example.company.myplanner.BudgetActivity;

import java.util.List;

/**
 * Created by Mohamed Sayed on 10/20/2017.
 */

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder> {
    private List<Budget> budgetList;
    private Activity activity;

    public BudgetAdapter(List<Budget> budgetList, Activity activity) {
        this.budgetList = budgetList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.setCommitment(budget.getCommitment());
        holder.setValue(budget.getValue());
        holder.setDeleteTextView();
        holder.position = position;
    }


    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int position;
        View mView;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
        }

        public void setCommitment(String commitment) {
            TextView textView = (TextView) mView.findViewById(R.id.commitmentTextView);
            textView.setText(commitment);

        }

        public void setValue(String value) {
            TextView textView = (TextView) mView.findViewById(R.id.valueTextView);
            textView.setText(value);
        }

        public void setDeleteTextView() {
            TextView textView = (TextView) mView.findViewById(R.id.deleteTextView);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.deleteTextView) {
                BudgetActivity mainActivity = (BudgetActivity) activity;
                mainActivity.deleteBudgetItem(position, budgetList.get(position).getKey());

            }
        }
    }
}
