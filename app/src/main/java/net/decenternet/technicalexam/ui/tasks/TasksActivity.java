package net.decenternet.technicalexam.ui.tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.decenternet.technicalexam.R;
import net.decenternet.technicalexam.data.TaskLocalServiceProvider;
import net.decenternet.technicalexam.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity implements TasksContract.View {

    private TasksContract.Presenter presenter;
    private final static String PREF_NAME = "MY_TASK";
    private RecyclerView rvTask;
    private TasksAdapter adapter;
    private FloatingActionButton fabBtn;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private TaskLocalServiceProvider provider;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        /**
         * Finish this simple task recording app.
         * The following are the remaining todos for this project:
         * 1. Make sure all defects are fixed.
         * 2. Showing a dialog to add/edit the task.
         * 3. Allowing the user to toggle it as completed.
         * 4. Allowing the user to delete a task.
         *
         */
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        provider = new TaskLocalServiceProvider(prefs);
        presenter = new TasksPresenter(provider,this);

        rvTask = (RecyclerView)findViewById(R.id.rvTask);
        rvTask.setHasFixedSize(true);
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        rvTask.addItemDecoration(new DividerItemDecoration(rvTask.getContext(), DividerItemDecoration.VERTICAL));

        fabBtn = (FloatingActionButton)findViewById(R.id.fab_btn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddTaskClicked();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        presenter.onGetAllTask();
    }

    @Override
    public void displayTasks(List<Task> tasks) {
        adapter = new TasksAdapter(this,tasks,presenter);
        rvTask.setAdapter(adapter);
    }

    @Override
    public void addTaskToList(final Task task) {
        dialog.dismiss();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addTask(task);
                progressDialog.dismiss();
            }
        },1000);
    }

    @Override
    public void removeTaskFromList(List<Task> tasks) {
        adapter = new TasksAdapter(this,tasks,presenter);
        rvTask.setAdapter(adapter);
    }

    @Override
    public void updateTaskInList(List<Task> tasks) {
        dialog.dismiss();
        adapter = new TasksAdapter(this,tasks,presenter);
        rvTask.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },1000);
    }

    @Override
    public void popupTaskAddingDialog() {
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_task_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText edtDescription = dialog.findViewById(R.id.edtDescription);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edtDescription.getText().toString();
                if(TextUtils.isEmpty(description)){
                    Toast.makeText(TasksActivity.this,"Please enter description",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    Task task = new Task();
                    task.setDescription(description);
                    task.setCompleted(false);
                    presenter.onSaveTaskClicked(task);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void popupTaskEditorDialog(final Task task, final int position, final List<Task> tasks) {
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_task_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText edtDescription = dialog.findViewById(R.id.edtDescription);
        edtDescription.setText(task.getDescription());
        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edtDescription.getText().toString();
                if(TextUtils.isEmpty(description)){
                    Toast.makeText(TasksActivity.this,"Please enter description",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    task.setId(task.getId());
                    task.setDescription(description);
                    task.setCompleted(task.isCompleted());
                    presenter.onUpdateTaskClicked(task,position,tasks);
                }
            }
        });

        dialog.show();
    }
}
