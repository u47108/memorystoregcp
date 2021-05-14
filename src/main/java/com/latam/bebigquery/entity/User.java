package com.latam.bebigquery.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RedisHash("User")
public class User implements Serializable {

  @Id
  private Long id;

  private String name;
  private String surname;
  private String age;

  public User(Long id, String name, String surname, String age) {

    this.id = id;
    this.name = name;
    this.surname = surname;
    this.age = age;

  }
}