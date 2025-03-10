package antifraud.dto;

import antifraud.model.SuspiciousIp;
import antifraud.validation.IPv4;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SuspiciousIpDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @IPv4
    private String ip;

    public SuspiciousIpDTO(long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public SuspiciousIpDTO() {
    }

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public SuspiciousIp toSuspiciousIp() {
        return new SuspiciousIp(ip);
    }

    public static SuspiciousIpDTO of(SuspiciousIp suspiciousIp) {
        return new SuspiciousIpDTO(suspiciousIp.getId(), suspiciousIp.getIp());
    }
}