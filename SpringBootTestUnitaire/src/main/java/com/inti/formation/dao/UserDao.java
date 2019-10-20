package com.inti.formation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inti.formation.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer>{
	public User findById(long id);
	public void deleteById(long id);
}
