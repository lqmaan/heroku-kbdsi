package icstar.kbdsi.apps.services;

import icstar.kbdsi.apps.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();


    User getUserByEmail(String email);

    ResponseEntity<User> updateUserById(User newUser, Long id);


    Page<User> findUsersContainingName(String name, boolean isDeleted, Integer pageNum, Integer pageSize);





}
