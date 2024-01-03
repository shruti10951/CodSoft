package com.example.todo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    EditText titleEt, descriptionEt, startDateEt, endDateEt;
    Button submitBtn;
    ImageView startImg, endImg;
    Spinner prioritySpinner;

    DatabaseHelper databaseHelper= new DatabaseHelper(this);
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        titleEt = findViewById(R.id.title_et);
        descriptionEt = findViewById(R.id.description_et);
        startDateEt = findViewById(R.id.start_date_et);
        endDateEt = findViewById(R.id.end_date_et);
        submitBtn = findViewById(R.id.submit_task_btn);

        startImg = findViewById(R.id.calendar_img_start);
        endImg = findViewById(R.id.calendar_img_end);

        prioritySpinner = findViewById(R.id.priority_spinner);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        startImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTaskActivity.this,
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

        endImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTaskActivity.this,
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

                if(checkInputData(title, startDate, endDate)){
                    sqLiteDatabase = databaseHelper.getWritableDatabase();
                    ContentValues contentValues= new ContentValues();
                    contentValues.put("title", title);
                    contentValues.put("description", description);
                    contentValues.put("startDate", startDate);
                    contentValues.put("endDate", endDate);
                    contentValues.put("priority", priority);
                    contentValues.put("status", "Inactive");

                    try{
                        sqLiteDatabase.insert("todo", null, contentValues);
                        Toast.makeText(AddTaskActivity.this, "Task Added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddTaskActivity.this, DashboardActivity.class));
                        finish();
                    }catch (SQLException e){
                        Toast.makeText(AddTaskActivity.this, "Something went wrong!\nPlease try again later.", Toast.LENGTH_SHORT).show();
                    }finally {
                        sqLiteDatabase.close();
                    }
                }

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