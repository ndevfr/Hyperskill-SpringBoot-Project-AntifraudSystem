package antifraud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "suspicious_ip")
public class SuspiciousIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "ip", nullable = false, unique = true)
    private String ip;

    public SuspiciousIp(String ip) {
        this.ip = ip;
    }

    public SuspiciousIp(){}

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }
}