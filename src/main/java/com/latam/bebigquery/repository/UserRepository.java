package com.latam.bebigquery.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.latam.bebigquery.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findByName(String name);

  List<User> findByNameAndSurname(String name, String surname);

}
