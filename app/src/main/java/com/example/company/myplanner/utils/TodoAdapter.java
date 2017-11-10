package com.example.company.myplanner.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.company.myplanner.PlannerActivity;
import com.example.company.myplanner.PlannerInfoActivity;
import com.example.company.myplanner.R;
import com.example.company.myplanner.AddPlannerActivity;

import java.util.List;

/**
 * Created by Mohamed Sayed on 10/20/2017.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {
    private List<Todo> todoList;
    private Activity activity;

    public TodoAdapter(List<Todo> todoList, Activity activity) {
        this.todoList = todoList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Todo todo = todoList.get(position);
        holder.setName(todo.getName());
        holder.setDateTime(todo.getDate());
        holder.setDeleteTextView();
        holder.setUpdateTextView();
        holder.position = position;
    }


    @Override
    public int getItemCount() {
        return todoList.size();
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

        public void setName(String name) {
            TextView textView = (TextView) mView.findViewById(R.id.nameTextView);
            textView.setText(name);
            textView.setOnClickListener(this);
        }

        public void setDateTime(String dateTime) {
            TextView textView = (TextView) mView.findViewById(R.id.dateTimeTextView);
            textView.setText(dateTime);
            textView.setOnClickListener(this);
        }

        public void setDeleteTextView() {
            TextView textView = (TextView) mView.findViewById(R.id.deleteTextView);
            textView.setOnClickListener(this);
        }

        public void setUpdateTextView() {
            TextView textView = (TextView) mView.findViewById(R.id.updateTextView);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent newIntent = new Intent(activity, AddPlannerActivity.class);
            newIntent.putExtra("todo", todoList.get(position));
            if (v.getId() == R.id.nameTextView || v.getId() == R.id.dateTimeTextView) {
                newIntent = new Intent(activity, PlannerInfoActivity.class);
                newIntent.putExtra("todo", todoList.get(position));
                activity.startActivity(newIntent);
            } else if (v.getId() == R.id.deleteTextView) {
                PlannerActivity mainActivity = (PlannerActivity) activity;
                mainActivity.deleteTodoItem(position, todoList.get(position).getKey());

            } else if (v.getId() == R.id.updateTextView) {
                newIntent.putExtra("id", todoList.get(position).getKey());
                activity.startActivity(newIntent);

            }
        }

    }

}
