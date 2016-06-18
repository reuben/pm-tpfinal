package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "service_requests")
public class ServiceRequest extends BaseDaoEnabled {
    public ServiceRequest(List<String> taskTypes, String description) {
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
    @DatabaseField
    private List<String> taskTypes;
    @DatabaseField
    private String description;
    @DatabaseField(foreign = true, canBeNull = false)
    private Client requester;
    @DatabaseField(foreign = true)
    private Technician technician;
}
