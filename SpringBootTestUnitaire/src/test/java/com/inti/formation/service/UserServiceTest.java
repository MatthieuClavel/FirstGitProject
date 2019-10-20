package com.inti.formation.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.inti.formation.SpringBootTestUnitaireApplication;
import com.inti.formation.dao.UserDao;
import com.inti.formation.dao.UserDaoTest;
import com.inti.formation.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTestUnitaireApplication.class)
public class UserServiceTest {

	@Autowired
	@Mock
	private UserService userServiceToMock;

	@Autowired
	@InjectMocks
	private UserService userServiceInjectMocks;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	@Mock
	private UserDao userDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void givenUsers_getHalfOfNumber() {
		LOGGER.info("----------- Testing givenUsers_getHalfOfNumber Method -----------");
		LOGGER.info("----------- Constructing Users -----------");
		// userServiceToMock = Mockito.mock(UserService.class); You can use this instead of using annotation
		LOGGER.info("----------- Mocking getAll() method of UserService -----------");
		Mockito.when(userServiceToMock.getAllUsers()).thenReturn(
				Arrays.asList(new User(10, "Bob"), new User(1, "Rob"), new User(2, "Job") ,new User(3, "Pob") ));
		LOGGER.info("----------- Method Mocked -----------");
		LOGGER.info("----------- Verifying results -----------");
		assertEquals(2, userService.getUserNbrHalf(userServiceToMock.getAllUsers()));
	}
	
	@Test
	public void trySave_InAddUser () {
		LOGGER.info("----------- Testing trySave_InAddUser -----------");
		User userTest = new User(10, "Bob");
		userServiceInjectMocks.addUser(userTest);
		LOGGER.info("----------- Verifying results -----------");
		verify(userDao, times(1)).save(userTest);
	}
}
