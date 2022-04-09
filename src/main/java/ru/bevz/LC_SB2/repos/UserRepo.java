package ru.bevz.LC_SB2.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.bevz.LC_SB2.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
