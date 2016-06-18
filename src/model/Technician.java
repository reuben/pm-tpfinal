package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

@DatabaseTable(tableName = "technicians")
public class Technician extends BaseDaoEnabled {
    public Technician() {
    }

    public Technician(String name, String email, String phone, List<String> taskTypes) {
        this.id = -1;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.taskTypes = taskTypes;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getTaskTypes() {
        return taskTypes;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String email;
    @DatabaseField
    private String phone;
    @DatabaseField
    private List<String> taskTypes;
}
