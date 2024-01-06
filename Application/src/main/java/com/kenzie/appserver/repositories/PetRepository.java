package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.PetRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface PetRepository extends CrudRepository<PetRecord, String> {
    PetRecord findByPetId(String petId);

    void deleteByPetId(String petId);

    Optional<PetRecord> findByPetIdAndEmail(String petId, String email);

    List<PetRecord> findPetByEmail(String email);

    List<PetRecord> findPetByPetId(String petId);

}