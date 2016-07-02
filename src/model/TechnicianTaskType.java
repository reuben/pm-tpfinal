package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "technician_tasktype")
public class TechnicianTaskType {
    public final static String TECHNICIAN_ID_FIELD = "technician_id";
    public final static String TASKTYPE_ID_FIELD = "tasktype_id";

    public TechnicianTaskType() {
    }

    public TechnicianTaskType(Technician technician, TaskType taskType) {
        this.technician = technician;
        this.taskType = taskType;
    }

    @DatabaseField(foreign = true, columnName = TECHNICIAN_ID_FIELD)
    private Technician technician;
    @DatabaseField(foreign = true, columnName = TASKTYPE_ID_FIELD)
    private TaskType taskType;
}
