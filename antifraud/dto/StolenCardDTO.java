package antifraud.dto;

import antifraud.model.StolenCard;
import antifraud.validation.CardNumber;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StolenCardDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @CardNumber
    private String number;

    public StolenCardDTO(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public StolenCardDTO() {
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public StolenCard toStolenCard() {
        return new StolenCard(number);
    }

    public static StolenCardDTO of(StolenCard stolenCard) {
        return new StolenCardDTO(stolenCard.getId(), stolenCard.getNumber());
    }
}