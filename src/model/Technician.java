package model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "technicians")
public class Technician extends BaseDaoEnabled {
    public Technician() {
    }

    public Technician(String name, String email, String phone, ArrayList<String> taskTypes) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(ArrayList<String> taskTypes) {
        this.taskTypes = taskTypes;
    }

    @Override
    public String toString() {
        return "Technician{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String email;
    @DatabaseField
    private String phone;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<String> taskTypes;
}
