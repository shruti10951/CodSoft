package com.example.todo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.helpers.DatabaseHelper;
import com.example.todo.helpers.PrefManager;
import com.example.todo.R;
import com.example.todo.helpers.TaskAdapter;
import com.example.todo.models.TaskModel;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

    TextView greetingsTxt;
    PrefManager prefManager;
    ImageView calendarImg, addTaskImg;

    Button allBtn, activeBtn, inactiveBtn, completedBtn;
    RecyclerView recyclerView;

    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper databaseHelper;
    Cursor cursor;
    TaskAdapter taskAdapter;
    ArrayList<TaskModel> list= new ArrayList<>();
    ArrayList<TaskModel> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        prefManager= new PrefManager(this);
        greetingsTxt = findViewById(R.id.hello_user_txt);
        calendarImg = findViewById(R.id.calendar_img);
        addTaskImg = findViewById(R.id.add_task_img);
        recyclerView = findViewById(R.id.task_rv);
        allBtn = findViewById(R.id.status_all_btn);
        inactiveBtn = findViewById(R.id.status_inactive_btn);
        activeBtn = findViewById(R.id.status_active_btn);
        completedBtn = findViewById(R.id.status_completed_btn);

        greetingsTxt.setText("Hello " + prefManager.getUsername());
        allBtn.setBackgroundResource(R.drawable.selected_border_layout);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        final String[] status = new String[1];

        for (int i = 0; i < 7; i++) {
            addCard(i);
        }

        taskAdapter = new TaskAdapter(DashboardActivity.this, list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(DashboardActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(taskAdapter);

        databaseHelper = new DatabaseHelper(this);

        refreshList("all");

        calendarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DashboardActivity.this,
                        R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String formattedDate = String.format("%04d-%02d-%02d", year, month+1, dayOfMonth);
                                filterList(formattedDate);
                                unselectAllCards();
                            }
                        },
                        year, month, date);
                datePickerDialog.show();
            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status[0] = "all";
                unselectAllCards();
                allBtn.setBackgroundResource(R.drawable.selected_border_layout);
                activeBtn.setBackgroundResource(R.drawable.border_layout);
                inactiveBtn.setBackgroundResource(R.drawable.border_layout);
                completedBtn.setBackgroundResource(R.drawable.border_layout);
                refreshList(status[0]);
            }
        });

        activeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status[0] = "Active";
                unselectAllCards();
                allBtn.setBackgroundResource(R.drawable.border_layout);
                activeBtn.setBackgroundResource(R.drawable.selected_border_layout);
                inactiveBtn.setBackgroundResource(R.drawable.border_layout);
                completedBtn.setBackgroundResource(R.drawable.border_layout);
                refreshList(status[0]);
            }
        });

        inactiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status[0] = "Inactive";
                unselectAllCards();
                allBtn.setBackgroundResource(R.drawable.border_layout);
                activeBtn.setBackgroundResource(R.drawable.border_layout);
                inactiveBtn.setBackgroundResource(R.drawable.selected_border_layout);
                completedBtn.setBackgroundResource(R.drawable.border_layout);
                refreshList(status[0]);
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status[0] = "Completed";
                unselectAllCards();
                allBtn.setBackgroundResource(R.drawable.border_layout);
                activeBtn.setBackgroundResource(R.drawable.border_layout);
                inactiveBtn.setBackgroundResource(R.drawable.border_layout);
                completedBtn.setBackgroundResource(R.drawable.selected_border_layout);
                refreshList(status[0]);
            }
        });

        addTaskImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AddTaskActivity.class));
            }
        });
    }

    private void refreshList(String status) {
        list.clear();

        sqLiteDatabase = databaseHelper.getReadableDatabase();

        String selection = (status.equals("all")) ? null : "status = ?";
        String[] selectionArgs = (status.equals("all")) ? null : new String[]{status};

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

            list.add(taskModel);

        }
        cursor.close();
        sqLiteDatabase.close();

        list.sort((task1, task2) -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = dateFormat.parse(task1.getStartDate());
                Date date2 = dateFormat.parse(task2.getStartDate());
                return date1.compareTo(date2);
            } catch (Exception e) {
                return 0;
            }
        });

        taskAdapter.notifyDataSetChanged();
    }

    private void filterList(String date) {
        filteredList.clear();
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        String selection = "(startDate <= ? AND endDate >= ?) OR (startDate = ? OR endDate = ?)";
        String[] selectionArgs = new String[]{date, date, date, date};

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

            filteredList.add(taskModel);

        }
        cursor.close();
        sqLiteDatabase.close();
        if(filteredList.isEmpty()){
            refreshList("all");
            unselectAllStatusCards();
            Toast.makeText(this, "No tasks for selected date", Toast.LENGTH_SHORT).show();
        }else{
            filteredList.sort((task1, task2) -> {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = dateFormat.parse(task1.getStartDate());
                    Date date2 = dateFormat.parse(task2.getStartDate());
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    return 0;
                }
            });
            taskAdapter.setTaskList(filteredList);
            taskAdapter.notifyDataSetChanged();
        }
    }

    private void addCard(int dayOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        LinearLayout linearCards = findViewById(R.id.linearCards);
        View cardView = LayoutInflater.from(this).inflate(R.layout.date_card, linearCards, false);

        String[] monthNames = new DateFormatSymbols().getShortMonths();
        String monthName = monthNames[month];
        String[] dayNames = new DateFormatSymbols().getShortWeekdays();
        String dayName = dayNames[dayOfWeek];

        TextView dateTxt = cardView.findViewById(R.id.date_txt);
        dateTxt.setText(String.valueOf(date));

        TextView monthTxt = cardView.findViewById(R.id.month_txt);
        monthTxt.setText(monthName);

        TextView dayTxt = cardView.findViewById(R.id.day_txt);
        dayTxt.setText(dayName);

        String formattedDate = String.format("%04d-%02d-%02d", year, month+1, date);

        linearCards.addView(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList(formattedDate);
                unselectAllCards();
                cardView.setBackgroundResource(R.drawable.selected_border_layout);
            }
        });

    }

    private void unselectAllCards() {
        LinearLayout linearCards = findViewById(R.id.linearCards);
        for (int i = 0; i < linearCards.getChildCount(); i++) {
            View cardView = linearCards.getChildAt(i);
            cardView.setBackgroundResource(R.drawable.border_layout);
        }
    }

    private void unselectAllStatusCards() {
        allBtn.setBackgroundResource(R.drawable.selected_border_layout);
        activeBtn.setBackgroundResource(R.drawable.border_layout);
        inactiveBtn.setBackgroundResource(R.drawable.border_layout);
        completedBtn.setBackgroundResource(R.drawable.border_layout);
    }

}