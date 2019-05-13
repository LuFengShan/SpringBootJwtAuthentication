package com.grokonez.jwtauthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grokonez.jwtauthentication.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户的名称来查询出用户
     * @param username 用户名称
     * @return
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据用户的名称来判断是否有这个用户
     * @param username 用户名称
     * @return
     */
    Boolean existsByUsername(String username);

    /**
     * 判断指定的email是否已经存在了
     * @param email eamil
     * @return
     */
    Boolean existsByEmail(String email);
}