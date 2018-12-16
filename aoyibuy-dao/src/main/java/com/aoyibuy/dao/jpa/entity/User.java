package com.aoyibuy.dao.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.aoyibuy.dao.jpa.UuidEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "t_user")
public class User extends UuidEntity {

    @Column(unique = true)
    private String name;
    
    private int age;
    
}
