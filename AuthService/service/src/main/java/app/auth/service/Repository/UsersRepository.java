package app.auth.service.Repository;

import app.auth.service.Entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserDetailsEntity,Long> {
Optional<UserDetailsEntity> findByEmail(String email);
    boolean existsByRole(String role);
    boolean existsByEmail(String email);

}