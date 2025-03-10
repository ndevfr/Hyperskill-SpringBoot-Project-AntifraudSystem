package antifraud.repository;

import antifraud.enums.Region;
import antifraud.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {

    @Query("""
        SELECT COUNT(DISTINCT t.region) FROM Transaction t\s
        WHERE t.number = :number\s
        AND NOT t.region = :region\s
        AND t.date BETWEEN :fromDateTime AND :toDateTime
        """)
    Long countBetweenDateTimesByNumberNotRegion(@Param("number") String number,
                                                @Param("region") Region region,
                                                @Param("fromDateTime") LocalDateTime fromDateTime,
                                                @Param("toDateTime") LocalDateTime toDateTime);

    @Query("""
        SELECT COUNT(DISTINCT t.ip) FROM Transaction t\s
        WHERE t.number = :number\s
        AND NOT t.ip = :ip\s
        AND t.date BETWEEN :fromDateTime AND :toDateTime
        """)
    Long countBetweenDateTimesByNumberNotIp(@Param("number") String number,
                                            @Param("ip") String ip,
                                            @Param("fromDateTime") LocalDateTime fromDateTime,
                                            @Param("toDateTime") LocalDateTime toDateTime);

    List<Transaction> findAllBy();

    List<Transaction> findAllByNumber(String number);

}
