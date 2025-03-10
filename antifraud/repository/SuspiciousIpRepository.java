package antifraud.repository;

import antifraud.model.SuspiciousIp;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspiciousIpRepository extends ListCrudRepository<SuspiciousIp, Long> {

    SuspiciousIp findByIp(String ip);

    boolean existsByIp(String ip);

    void deleteByIp(String ip);

    List<SuspiciousIp> findAllByOrderById();

}
