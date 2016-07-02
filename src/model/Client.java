package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

@DatabaseTable(tableName = "clients")
public class Client {
    public Client() {
    }

    public Client(String name, String id, String CPF, String address, LocalDate birthdate, String email, String phone) {
        this.name = name;
        this.id = id;
        this.CPF = CPF;
        this.address = address;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
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

    @DatabaseField
    private String name;
    @DatabaseField
    private String id;
    @DatabaseField(id = true)
    private String CPF;
    @DatabaseField
    private String address;
    @DatabaseField(persisterClass = LocalDateType.class)
    private LocalDate birthdate;
    @DatabaseField
    private String email;
    @DatabaseField
    private String phone;
}
