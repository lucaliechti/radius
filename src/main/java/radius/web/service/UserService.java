package radius.web.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepo;

    public UserService(JDBCUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<String> findUuidByEmail(String email) {
        try {
            String uuid = userRepo.findUuidByEmail(email);
            return Optional.of(uuid);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findEmailByUuid(String uuid) {
        try {
            String email = userRepo.findEmailByUuid(uuid);
            return Optional.of(email);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
