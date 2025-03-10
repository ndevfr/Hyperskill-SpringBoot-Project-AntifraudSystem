package antifraud.repository;

import antifraud.model.AppUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<AppUser, Long> {

    Optional<AppUser> findByUserDetailsUsernameIgnoreCase(String username);

    boolean existsByUserDetailsUsernameIgnoreCase(String username);

    List<AppUser> findAllByOrderById();

    void deleteByUserDetailsUsername(String username);

}
