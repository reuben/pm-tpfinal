package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDateTime;

@DatabaseTable(tableName = "service_requests")
public class ServiceRequest {
    public static final String STATUS_FIELD = "status";

    public enum Status {
        Registered("Cadastrada"),
        AwaitingEstimate("Aguardando Orçamento"),
        AwaitingApproval("Aguardando Aprovação do Cliente"),
        Approved("Aprovada"),
        InProgress("Em andamento"),
        Finished("Concluída"),
        AwaitingPayment("Em cobrança"),
        Closed("Encerrada"),
        Canceled("Cancelada");

        Status(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

        private String description;
    }

    public ServiceRequest() {
    }

    public ServiceRequest(Client requester, String description) {
        this.requester = requester;
        this.description = description;
        this.status = Status.Registered;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getRequester() {
        return requester;
    }

    public void setRequester(Client requester) {
        this.requester = requester;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ServiceEstimate getEstimate() {
        return estimate;
    }

    public void setEstimate(ServiceEstimate estimate) {
        this.estimate = estimate;
    }

    public LocalDateTime getServiceConcludedTime() {
        return serviceConcludedTime;
    }

    public void setServiceConcludedTime(LocalDateTime serviceConcludedTime) {
        this.serviceConcludedTime = serviceConcludedTime;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String toString() {
        return requester.getName() + " - " + description;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String description;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Client requester;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Technician technician;
    @DatabaseField(columnName = STATUS_FIELD)
    private Status status;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private ServiceEstimate estimate;
    @DatabaseField(persisterClass = LocalDateTimeType.class)
    private LocalDateTime serviceConcludedTime;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Payment payment;
}
