package com.eightbit.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eightbit.books.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByLastNameContaining(String name);

	List<User> findByFirstNameContaining(String name);

	User findByUserId(int userId);
}
