package net.decenternet.technicalexam.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.decenternet.technicalexam.domain.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskLocalServiceProvider implements TaskLocalService {

    private final SharedPreferences sharedPreferences;
    private Collection<Task> localTasks;

    public TaskLocalServiceProvider(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        localTasks = new Gson()
                .fromJson(
                        sharedPreferences.getString("tasks", "[]"),
                        new TypeToken<List<Task>>(){}.getType()
                );
    }

    @Override
    public void save(Task task) {
        ArrayList<Task> tasks = new ArrayList<>(localTasks);
        task.setId(getNextId());
        tasks.add(task);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tasks", new Gson().toJson(tasks));
        editor.apply();

        localTasks = tasks;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(localTasks);
    }

    @Override
    public void update(Task task, int position) {
        ArrayList<Task> tasks = new ArrayList<>(localTasks);
        tasks.set(position,task);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tasks", new Gson().toJson(tasks));
        editor.apply();

        localTasks = tasks;
    }

    @Override
    public void delete(ArrayList<Task> tasks, Task taskToDelete) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tasks", new Gson().toJson(tasks));
        editor.apply();

        localTasks.remove(taskToDelete);
    }

    private Integer getNextId() {
        return localTasks.size() + 1;
    }

}
