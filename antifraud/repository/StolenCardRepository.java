package antifraud.repository;

import antifraud.model.StolenCard;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StolenCardRepository extends ListCrudRepository<StolenCard, Long> {

    StolenCard findByNumber(String number);

    boolean existsByNumber(String number);

    void deleteByNumber(String number);

    List<StolenCard> findAllByOrderById();

}
