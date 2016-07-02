package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "payments")
public class Payment {
    public enum PaymentType {
        CreditCard("Cartão de crédito"),
        BankDeposit("Depósito bancário");

        PaymentType(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

        private String description;
    }

    public Payment() {
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public void setRequest(ServiceRequest request) {
        this.request = request;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private ServiceRequest request;
    @DatabaseField
    private String receiptNumber;
    @DatabaseField
    private PaymentType type;
    @DatabaseField
    private String bankName;
}
