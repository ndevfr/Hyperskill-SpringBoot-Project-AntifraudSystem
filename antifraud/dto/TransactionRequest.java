package antifraud.dto;

import antifraud.validation.CardNumber;
import antifraud.validation.IPv4;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransactionRequest {

    @NotNull
    @Positive
    private Long amount;

    @IPv4
    private String ip;

    @CardNumber
    private String number;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
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
}
