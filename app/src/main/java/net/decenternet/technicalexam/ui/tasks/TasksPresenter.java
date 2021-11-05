package net.decenternet.technicalexam.ui.tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.decenternet.technicalexam.R;
import net.decenternet.technicalexam.data.TaskLocalService;
import net.decenternet.technicalexam.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksPresenter implements TasksContract.Presenter {

    private final TaskLocalService taskLocalService;
    private final TasksContract.View view;

    public TasksPresenter(TaskLocalService taskLocalService, TasksContract.View view) {
        this.taskLocalService = taskLocalService;
        this.view = view;
    }

    @Override
    public void onGetAllTask() {
        List<Task> tasks =  taskLocalService.findAll();
        view.displayTasks(tasks);
    }

    @Override
    public void onAddTaskClicked() {
        view.popupTaskAddingDialog();
    }

    @Override
    public void onSaveTaskClicked(Task task) {
        taskLocalService.save(task);
        view.addTaskToList(task);
    }

    @Override
    public void onTaskChecked(Task task, int position) {
        taskLocalService.update(task,position);
    }

    @Override
    public void onTaskUnchecked(Task task, int position) {
        taskLocalService.update(task,position);
    }

    @Override
    public void onDeleteTaskClicked(ArrayList<Task> tasks, Task taskToDelete) {
        taskLocalService.delete(tasks,taskToDelete);
        view.removeTaskFromList(tasks);
    }

    @Override
    public void onUpdateTaskClicked(Task task, int position, List<Task> tasks) {
        taskLocalService.update(task,position);
        view.updateTaskInList(tasks);
    }

    @Override
    public void showEditorDialog(Task task, int position, List<Task> tasks) {
        view.popupTaskEditorDialog(task, position, tasks);
    }
}
