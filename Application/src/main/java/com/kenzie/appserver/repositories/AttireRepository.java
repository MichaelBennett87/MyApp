/*
package com.kenzie.appserver.repositories;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.kenzie.appserver.repositories.model.AttireRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface AttireRepository extends CrudRepository<AttireRecord<S>, String> {
    void setAttire(AttireRecord<String> attire);
    AttireRecord<String> findByPetId(String petId);

    void deleteByPetId(String petId);

    AttireRecord<String> findByPetIdAndEmail(String petId, String email);

    List<AttireRecord<S>> findPetByEmail(String email);


}
*/
