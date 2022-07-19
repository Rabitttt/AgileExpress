package com.obss.AgileExpress.repository;


import com.obss.AgileExpress.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
    User findUserByEmail(String email);

    User findByUsername(String username);
}
