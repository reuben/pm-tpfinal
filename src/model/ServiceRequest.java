package model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "service_requests")
public class ServiceRequest {
    public ServiceRequest() {
    }

    public ServiceRequest(ArrayList<String> taskTypes, String description) {
        this.id = -1;
        this.taskTypes = taskTypes;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public List<String> getTaskTypes() {
        return taskTypes;
    }

    public String getDescription() {
        return description;
    }

    public Client getRequester() {
        return requester;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<String> taskTypes;
    @DatabaseField
    private String description;
    @DatabaseField(foreign = true, canBeNull = false)
    private Client requester;
    @DatabaseField(foreign = true)
    private Technician technician;
}
