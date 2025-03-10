package antifraud.model;

import antifraud.enums.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "card_limits")
public class CardLimits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "number", nullable = false, unique = true)
    private String number;
    @Column(name = "allowed", nullable = false)
    private long allowed;
    @Column(name = "manual", nullable = false)
    private long manual;

    public CardLimits(String number) {
        this.number = number;
        this.allowed = 200;
        this.manual = 1500;
    }

    public CardLimits() {
    }

    public long getId() {
        return id;
    }

    public long getAllowed() {
        return allowed;
    }

    public long getManual() {
        return manual;
    }

    public void setAllowed(long allowed) {
        this.allowed = allowed;
    }

    public void setManual(long manual) {
        this.manual = manual;
    }

    public void changeLimit(TransactionType type, String operation, long amount) {
        switch (type) {
            case TransactionType.MANUAL_PROCESSING:
                if (operation.equals("INC")) {
                    this.manual = (long) Math.ceil(0.8 * this.manual + 0.2 * amount);
                } else {
                    this.manual = (long) Math.ceil(0.8 * this.manual - 0.2 * amount);
                }
                break;
            case TransactionType.ALLOWED:
                if (operation.equals("INC")) {
                    this.allowed = (long) Math.ceil(0.8 * this.allowed + 0.2 * amount);
                } else {
                    this.allowed = (long) Math.ceil(0.8 * this.allowed - 0.2 * amount);
                }
                break;
        }
    }
}
