package net.decenternet.technicalexam.ui.tasks;

import android.content.Context;

import net.decenternet.technicalexam.domain.Task;

import java.util.ArrayList;
import java.util.List;

public interface TasksContract {
    
    public interface View {
        
        void displayTasks(List<Task> tasks);
        void addTaskToList(Task task);
        void removeTaskFromList(List<Task> tasks);
        void updateTaskInList(List<Task> tasks);
        void popupTaskAddingDialog();
        void popupTaskEditorDialog(Task task, int position, List<Task> tasks);
        
    }
    
    public interface Presenter {

        void onGetAllTask();
        void onAddTaskClicked();
        void onSaveTaskClicked(Task task);
        void onTaskChecked(Task task, int position);
        void onTaskUnchecked(Task task, int position);
        void onDeleteTaskClicked(ArrayList<Task> tasks, Task taskToDelete);
        void onUpdateTaskClicked(Task task, int position, List<Task> tasks);
        void showEditorDialog(Task task, int position, List<Task> tasks);
    }
    
}
