package antifraud.repository;

import antifraud.model.CardLimits;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CardLimitsRepository extends ListCrudRepository<CardLimits, Long> {

    CardLimits findByNumber(String number);

    boolean existsByNumber(String number);

    void deleteByNumber(String number);

    List<CardLimits> findAllByOrderById();

}