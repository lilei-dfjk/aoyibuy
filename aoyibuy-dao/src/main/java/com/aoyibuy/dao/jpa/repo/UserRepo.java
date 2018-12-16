package com.aoyibuy.dao.jpa.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.aoyibuy.dao.jpa.entity.User;


public interface UserRepo extends PagingAndSortingRepository<User, String> {

    User findByName(String name);
    
}