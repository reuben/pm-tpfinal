package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "servicerequest_tasktype")
public class ServiceRequestTaskType {
    public final static String SERVICEREQUEST_ID_FIELD = "servicerequest_id";
    public final static String TASKTYPE_ID_FIELD = "tasktype_id";

    public ServiceRequestTaskType() {
    }

    public ServiceRequestTaskType(ServiceRequest serviceRequest, TaskType taskType) {
        this.serviceRequest = serviceRequest;
        this.taskType = taskType;
    }

    @DatabaseField(foreign = true, columnName = SERVICEREQUEST_ID_FIELD)
    private ServiceRequest serviceRequest;
    @DatabaseField(foreign = true, columnName = TASKTYPE_ID_FIELD)
    private TaskType taskType;
}
