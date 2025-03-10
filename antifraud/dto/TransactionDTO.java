package antifraud.dto;

import antifraud.enums.Region;
import antifraud.enums.TransactionType;
import antifraud.model.Transaction;
import antifraud.validation.CardNumber;
import antifraud.validation.IPv4;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import antifraud.model.Feedback;

import java.time.LocalDateTime;

public class TransactionDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long transactionId;
    @NotNull
    private long amount;
    @IPv4
    private String ip;
    @CardNumber
    private String number;
    @NotNull
    private Region region;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TransactionType result;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String feedback;

    public TransactionDTO(long id, long amount, String ip, String number, Region region, LocalDateTime date, TransactionType result, String feedback) {
        this.transactionId = id;
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
        this.result = result;
        this.feedback = feedback;
    }

    public TransactionDTO(){}

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long id) {
        this.transactionId = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getResult() {
        return result;
    }

    public void setResult(TransactionType type) {
        this.result = type;
    }

    public String getFeedback() {
        return feedback;
    }

    public Transaction toTransaction() {
        return new Transaction(amount, ip, number, region, date, result);
    }

    public static TransactionDTO of(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getAmount(), transaction.getIp(), transaction.getNumber(), transaction.getRegion(), transaction.getDate(), transaction.getType(), transaction.getFeedback().getMessage());
    }
}