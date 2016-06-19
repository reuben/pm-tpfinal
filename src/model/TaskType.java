package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "task_types")
public class TaskType {
    public final static String ID_FIELD = "name";

    public TaskType() {

    }

    public TaskType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @DatabaseField(id = true, columnName = ID_FIELD)
    private String name;
}
