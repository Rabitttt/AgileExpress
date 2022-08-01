package com.obss.AgileExpress.repository;


import com.obss.AgileExpress.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    User findUserByEmail(String email);

    User findByUsername(String username);
}
