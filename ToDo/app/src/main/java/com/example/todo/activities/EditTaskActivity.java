package com.example.todo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todo.R;
import com.example.todo.helpers.DatabaseHelper;
import com.example.todo.models.TaskModel;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    int id;
    EditText titleEt, descriptionEt, startDateEt, endDateEt;
    ImageView startDateImg, endDateImg;
    Spinner prioritySpinner, statusSpinner;
    Button deleteBtn, submitBtn;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        id= getIntent().getIntExtra("id", 0);
        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        titleEt = findViewById(R.id.title_et);
        descriptionEt = findViewById(R.id.description_et);
        startDateEt = findViewById(R.id.start_date_et);
        endDateEt = findViewById(R.id.end_date_et);
        prioritySpinner = findViewById(R.id.priority_spinner);
        statusSpinner = findViewById(R.id.status_spinner);

        startDateImg = findViewById(R.id.edit_start_date_img);
        endDateImg = findViewById(R.id.edit_end_date_img);

        deleteBtn = findViewById(R.id.delete_task_btn);
        submitBtn = findViewById(R.id.submit_task_btn);

        String selection = "id = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        cursor = sqLiteDatabase.query("todo", null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()){
            TaskModel taskModel= new TaskModel();

            int idIndex = cursor.getColumnIndex("id");
            taskModel.setId(cursor.getInt(idIndex));

            int titleIndex = cursor.getColumnIndex("title");
            taskModel.setTitle(cursor.getString(titleIndex));

            int descriptionIndex = cursor.getColumnIndex("description");
            taskModel.setDescription(cursor.getString(descriptionIndex));

            int startDateIndex = cursor.getColumnIndex("startDate");
            taskModel.setStartDate(cursor.getString(startDateIndex));

            int endDateIndex = cursor.getColumnIndex("endDate");
            taskModel.setEndDate(cursor.getString(endDateIndex));

            int priorityIndex = cursor.getColumnIndex("priority");
            taskModel.setPriority(cursor.getString(priorityIndex));

            int statusIndex = cursor.getColumnIndex("status");
            taskModel.setStatus(cursor.getString(statusIndex));
            titleEt.setText(taskModel.getTitle());
            descriptionEt.setText(taskModel.getDescription());
            startDateEt.setText(taskModel.getStartDate());
            endDateEt.setText(taskModel.getEndDate());
            if(taskModel.getPriority() == "High"){
                prioritySpinner.setSelection(2);
            }else if(taskModel.getPriority() == "Medium"){
                prioritySpinner.setSelection(1);
            }else{
                prioritySpinner.setSelection(0);
            }
            if(taskModel.getStatus() == "Active"){
                statusSpinner.setSelection(2);
            }else if(taskModel.getPriority() == "Inactive"){
                statusSpinner.setSelection(1);
            }else{
                statusSpinner.setSelection(0);
            }
        }

        //if we dont want user to edit start and end date we can comment this part out
        startDateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTaskActivity.this,
                        R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String formattedDate= String.format("%04d-%02d-%02d", year, month+1, dayOfMonth);
                                startDateEt.setText(formattedDate);
                            }
                        },
                        year, month, date);
                datePickerDialog.show();
            }
        });

        endDateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTaskActivity.this,
                        R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String formattedDate= String.format("%04d-%02d-%02d", year, month+1, dayOfMonth);
                                endDateEt.setText(formattedDate);
                            }
                        },
                        year, month, date);
                datePickerDialog.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();
                String startDate = startDateEt.getText().toString().trim();
                String endDate = endDateEt.getText().toString().trim();
                String priority = prioritySpinner.getSelectedItem().toString();
                String status = statusSpinner.getSelectedItem().toString();

                if(checkInputData(title, startDate, endDate)){
                    sqLiteDatabase = databaseHelper.getWritableDatabase();
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("title", title);
                    contentValues.put("description", description);
                    contentValues.put("startDate", startDate);
                    contentValues.put("endDate", endDate);
                    contentValues.put("priority", priority);
                    contentValues.put("status", status);

                    try{
                        sqLiteDatabase.update("todo", contentValues, selection, selectionArgs);
                        Toast.makeText(EditTaskActivity.this, "Task Edited!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditTaskActivity.this, DashboardActivity.class));
                        finish();
                    }catch (SQLException e){
                        Toast.makeText(EditTaskActivity.this, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                    }finally {
                        sqLiteDatabase.close();
                    }
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this task?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLiteDatabase = databaseHelper.getWritableDatabase();
                        sqLiteDatabase.delete("todo",selection, selectionArgs);
                        sqLiteDatabase.close();
                        Toast.makeText(EditTaskActivity.this, "Task Deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditTaskActivity.this, DashboardActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private boolean checkInputData(String title, String startDate, String endDate) {
        if(title.isEmpty()){
            titleEt.setError("Please enter title of the task!");
            titleEt.requestFocus();
            return false;
        }else if(startDate.isEmpty()){
            startDateEt.setError("Please enter start date of the task!");
            startDateEt.requestFocus();
            return false;
        }else if(endDate.isEmpty()){
            endDateEt.setError("Please enter end date of the task!");
            endDateEt.requestFocus();
            return false;
        }else {
            return true;
        }
    }
}