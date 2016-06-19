package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "technicians")
public class Technician extends BaseDaoEnabled {
    public static final String ID_FIELD = "id";

    public Technician() {
    }

    public Technician(String name, String email, String phone) {
        this.id = -1;
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    @Override
    public String toString() {
        return name;
    }

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private long id;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String email;
    @DatabaseField
    private String phone;
}
