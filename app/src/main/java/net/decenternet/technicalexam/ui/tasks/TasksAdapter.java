package net.decenternet.technicalexam.ui.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.decenternet.technicalexam.R;
import net.decenternet.technicalexam.data.TaskLocalServiceProvider;
import net.decenternet.technicalexam.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context context;
    private List<Task> tasks;
    private TasksContract.Presenter presenter;

    public TasksAdapter(Context context, List<Task> tasks, TasksContract.Presenter presenter) {
        this.context = context;
        this.tasks = tasks;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public TasksAdapter.TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tasks_list_layout, null);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TasksAdapter.TasksViewHolder holder, final int position) {
        final Task task = tasks.get(position);
        holder.tvId.setText("Task No: " + task.getId());
        holder.tvDescription.setText("Description: " + task.getDescription());
        holder.aSwitch.setChecked(task.isCompleted());
        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.isCompleted()) {
                    Task newTask = new Task();
                    newTask.setId(task.getId());
                    newTask.setDescription(task.getDescription());
                    newTask.setCompleted(false);
                    tasks.set(position,newTask);
                    notifyDataSetChanged();
                    ArrayList<Task> tasksList = new ArrayList<>(tasks);
                    presenter.onTaskUnchecked(newTask,position);
                }else{
                    Task newTask = new Task();
                    newTask.setId(task.getId());
                    newTask.setDescription(task.getDescription());
                    newTask.setCompleted(true);
                    tasks.set(position,newTask);
                    notifyDataSetChanged();
                    ArrayList<Task> tasksList = new ArrayList<>(tasks);
                    presenter.onTaskChecked(newTask,position);
                }
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showEditorDialog(task,position,tasks);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(position);
                notifyDataSetChanged();
                ArrayList<Task> tasksList = new ArrayList<>(tasks);
                presenter.onDeleteTaskClicked(tasksList,task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void addTask(Task task){
        tasks.add(task);
        notifyDataSetChanged();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView tvDescription;
        private final ImageView imgEdit;
        private final ImageView imgDelete;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private final Switch aSwitch;

        public TasksViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            aSwitch = itemView.findViewById(R.id.simpleSwitch);
        }

    }
}
