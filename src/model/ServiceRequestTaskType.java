package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//TODO: register DAO, create table
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

    //TODO: check if we can add ForeignCollections to ServiceRequest and TaskType to make using this many-to-many relation easier

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(foreign = true, columnName = SERVICEREQUEST_ID_FIELD)
    private ServiceRequest serviceRequest;
    @DatabaseField(foreign = true, columnName = TASKTYPE_ID_FIELD)
    private TaskType taskType;
}
