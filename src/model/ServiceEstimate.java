package model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "service_estimates")
public class ServiceEstimate {
    public static class MaterialEntry implements Serializable {
        public long quantity;
        public String description;
        public double value;

        public MaterialEntry(long quantity, String description, double value) {
            this.quantity = quantity;
            this.description = description;
            this.value = value;
        }

        public Object get(int col) {
            switch (col) {
                case 0: return quantity;
                case 1: return description;
                case 2: return value;
            }
            throw new IllegalArgumentException("Invalid column value");
        }

        public void set(Object object, int col) {
            switch (col) {
                case 0: quantity = (long)object;
                case 1: description = (String)object;
                case 2: value = (double)object;
            }
            throw new IllegalArgumentException("Invalid column value");
        }
    }

    public ServiceEstimate() {

    }

    public ServiceEstimate(ServiceRequest request) {
        this.request = request;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setRequest(ServiceRequest request) {
        this.request = request;
    }

    public ArrayList<MaterialEntry> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<MaterialEntry> materials) {
        this.materials = materials;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public double getFinalValue() {
        return finalValue;
    }

    public double getServiceHours() {
        return serviceHours;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setServiceHours(double serviceHours) {
        this.serviceHours = serviceHours;
    }

    public void applyTax() {
        this.finalValue = this.value * (1+this.serviceTax);
    }

    @DatabaseField(generatedId = true)
    private long id = 0;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private ServiceRequest request;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<MaterialEntry> materials;
    @DatabaseField
    private double serviceHours;
    @DatabaseField
    private double hourlyRate;
    @DatabaseField
    private double value;
    @DatabaseField
    private double serviceTax = 0.05;
    @DatabaseField
    private double finalValue;
}
