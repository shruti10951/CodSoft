package com.example.todo.helpers;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.activities.EditTaskActivity;
import com.example.todo.models.TaskModel;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    ArrayList<TaskModel> arrayList;
    SQLiteDatabase database;
    DatabaseHelper databaseHelper;

    public TaskAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TaskModel taskModel = arrayList.get(position);

        holder.titleTxt.setText(taskModel.getTitle());
        holder.endDateTxt.setText(taskModel.getStartDate() + " to " + taskModel.getEndDate());
        holder.priorityTxt.setText("Priority: " + taskModel.getPriority());
        holder.statusTxt.setText(taskModel.getStatus());

        if(taskModel.getStatus().equals("Active")){
            holder.statusImg.setBackgroundResource(R.drawable.active_circle_drawable);
        }else if(taskModel.getStatus().equals("Inactive")){
            holder.statusImg.setBackgroundResource(R.drawable.inactive_circle_drawable);
        }else{
            holder.statusImg.setBackgroundResource(R.drawable.completed_circle_drawable);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, EditTaskActivity.class);
                intent.putExtra("id", taskModel.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setTaskList(ArrayList<TaskModel> newList) {
        arrayList.clear();
        arrayList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTxt, endDateTxt, priorityTxt, statusTxt;
        ImageView statusImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.task_txt);
            endDateTxt = itemView.findViewById(R.id.task_end_date_txt);
            priorityTxt = itemView.findViewById(R.id.task_priority_txt);
            statusTxt = itemView.findViewById(R.id.task_status_txt);
            statusImg = itemView.findViewById(R.id.status_img);

        }

    }
}