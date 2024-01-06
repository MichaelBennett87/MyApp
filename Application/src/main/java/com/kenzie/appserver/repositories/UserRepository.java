package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.UserRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRepository extends CrudRepository<UserRecord, String> {
    UserRecord findByEmail(String email);

    UserRecord findByAdoptedPetId(String petId);

    boolean existsByEmail(String email);



    // Add any additional methods or queries specific to UserRecord if needed
}
