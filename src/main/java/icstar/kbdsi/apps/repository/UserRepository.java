package icstar.kbdsi.apps.repository;

import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByDeleted(boolean isDeleted);
    Page<User> findByNameContainingAndDeleted(String name, boolean isDeleted, PageRequest pageable, Sort sort);
    User findByEmail(String email);
    User findByName(String name);
    void findAllByName(String name, PageRequest pageRequest);


}
