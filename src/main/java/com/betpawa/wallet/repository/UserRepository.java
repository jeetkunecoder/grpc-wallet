package com.betpawa.wallet.repository;

import com.betpawa.wallet.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional(readOnly = true)
    @Query("select id from User u where u.name=:name")
    User findUserByName(@Param("name") String name);
}