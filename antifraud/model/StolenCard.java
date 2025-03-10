package antifraud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stolen_cards")
public class StolenCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    public StolenCard(String number) {
        this.number = number;
    }

    public StolenCard(){}

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}