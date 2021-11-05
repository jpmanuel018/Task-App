package net.decenternet.technicalexam.data;

import net.decenternet.technicalexam.domain.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskLocalService {

    void save(Task task);
    List<Task> findAll();
    void update(Task task, int position);
    void delete(ArrayList<Task> tasks, Task taskToDelete);
}
