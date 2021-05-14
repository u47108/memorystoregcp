package com.latam.bebigquery.repository;

import com.latam.bebigquery.entity.User;

public interface UserDao {

  public Boolean saveUser(User user) ;
  public User findByName(String name) ;


}