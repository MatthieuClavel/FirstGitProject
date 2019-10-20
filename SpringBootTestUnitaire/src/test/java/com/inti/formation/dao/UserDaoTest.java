package com.inti.formation.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.inti.formation.SpringBootTestUnitaireApplication;
import com.inti.formation.dao.UserDao;
import com.inti.formation.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTestUnitaireApplication.class)
public class UserDaoTest {
	
	@Autowired
	private UserDao userDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	@Test
	public void givenEntityRepository_whenSaving() {
		LOGGER.info("----------- Testing givenEntityRepository_whenSaving Method -----------");
		System.out.println(userDao);
		User addedUser = userDao.save(new User(10, "Bob"));
		LOGGER.info("----------- User saved -----------");
		LOGGER.info("----------- Searching for User -----------");
		User foundUser = userDao.findById(addedUser.getId());
		LOGGER.info("----------- User found -----------");
		LOGGER.info("----------- Verifying User -----------");
		assertNotNull(foundUser);
		assertEquals(addedUser.getUserName(), foundUser.getUserName());
		LOGGER.info("----------- User verified -----------");
		
	}
	
}
