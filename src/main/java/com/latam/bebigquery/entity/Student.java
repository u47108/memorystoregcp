package com.latam.bebigquery.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Student")
public class Student implements Serializable {

@Id
@NotBlank
private String id;

@NotBlank
private String name;

@Min(2018)
private int year;

//getter and setter
}