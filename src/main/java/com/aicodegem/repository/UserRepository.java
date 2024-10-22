//mongoDB와 상호작용하는 리포지토리
package com.aicodegem.repository;

import com.aicodegem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
