package com.inti.formation.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inti.formation.SpringBootTestUnitaireApplication;
import com.inti.formation.dao.UserDaoTest;
import com.inti.formation.entity.User;
import com.inti.formation.service.UserService;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTestUnitaireApplication.class)
public class UserControllerTest {

	@Autowired
	WebApplicationContext webApplicationContext; // Crée un context pour exécuter des requêtes web
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	UserService userServiceMocked;
	
	
	protected MockMvc mvc; // Used to mock the web content
	protected String uri; // Used for the webservice adressing. You need to initiate in the subclasses contructors
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); // Crée une fausse image du webApplicationContext
		MockitoAnnotations.initMocks(this);
	}
	
	@Autowired
	private UserService userService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	public UserControllerTest() {
		super();
		this.uri = "/user";
	}
	
	@Test
	public void getAllEntityList() {
		MvcResult mvcResult;
		LOGGER.info("----------- Testing getAllEntity Method -----------");
		try {
			LOGGER.info("----------- Constructing and Saving User -----------");
			userService.addUser(new User(2, "Paul")); // Mauvaise idée : il faudrait faire un mockito.when ...
			LOGGER.info("----------- Mocking Context Webservice -----------");
			mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri + "/all")
					.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
			LOGGER.info("----------- Getting HTTP Status -----------");
			int status = mvcResult.getResponse().getStatus();
			LOGGER.info("----------- Verifying HTTP Status -----------");
			assertEquals(200, status);
			LOGGER.info("----------- Getting HTTP Response -----------");
			String content = mvcResult.getResponse().getContentAsString();
			LOGGER.info("----------- Deserializing JSON Response -----------");
			User[] userList = this.mapFromJson(content, User[].class);
			assertTrue(userList.length > 0);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getStatusWhenAddingUser() {
		LOGGER.info("----------- Testing server status when adding user -----------");
		LOGGER.info("----------- Constructing User -----------");
		User user = new User(50, "Sala7");
		
		MvcResult mvcResult;
		try {
			LOGGER.info("----------- Serializing User Object -----------");
			String inputJson = this.mapToJson(user);
			LOGGER.info("----------- Mocking Context Webservice and Invoking the Webservice -----------");
			mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/adduser")
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
//			mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/adduser")
//					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
//					.andExpect(status().isOk()).andReturn();
			// This up can be used to replace the below section
			LOGGER.info("----------- Getting HTTP Status -----------");
			int status = mvcResult.getResponse().getStatus();
			LOGGER.info("----------- Verifying HTTP Status -----------");
			assertEquals(200, status);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void checkForMethodWhenAddingUser() {
		LOGGER.info("----------- Testing called method when adding user -----------");
		LOGGER.info("----------- Constructing User -----------");
		User user = new User(50, "Sala7");
		LOGGER.info("----------- Injecting Mocking User -----------");
		userController.addNewUser(user);
		LOGGER.info("----------- Verifying results -----------");
		verify(userServiceMocked, times(1)).addUser(user);
	}
	
	@Test
	public void createEntity() {
		LOGGER.info("----------- Testing createEntity Method -----------");
		LOGGER.info("----------- Constructing User -----------");
		User user = new User(50, "Sala7");
		
		MvcResult mvcResult;
		try {
			LOGGER.info("----------- Serializing User Object -----------");
			String inputJson = this.mapToJson(user);
			LOGGER.info("----------- Mocking Context Webservice and Invoking the Webservice -----------");
			mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri + "/adduser")
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			LOGGER.info("----------- Getting HTTP Status -----------");
			int status = mvcResult.getResponse().getStatus();
			LOGGER.info("----------- Verifying HTTP Status -----------");
			assertEquals(200, status);
			LOGGER.info("----------- Searching for User -----------");
			User userFound = userService.getUserById(new Long(50));
			LOGGER.info("----------- Verifying User -----------");
			assertEquals(userFound.getUserName(), user.getUserName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateEntity() {
		MvcResult mvcResult;
		LOGGER.info("----------- Testing updateEntity Method -----------");
		try {
			LOGGER.info("----------- Constructing User -----------");
			User oldUser = new User(2, "Paul");
			LOGGER.info("----------- Saving User -----------");
			userService.addUser(oldUser);
			LOGGER.info("----------- Modifying User -----------");
			User newUser = new User(2, "Raul");
			LOGGER.info("----------- Serializing User Object -----------");
			String inputJson = this.mapToJson(newUser);
			LOGGER.info("----------- Mocking Context Webservice and Invoking the Webservice -----------");
			mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri + "/2")
					.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
			LOGGER.info("----------- Getting HTTP Status -----------");
			int status = mvcResult.getResponse().getStatus();
			LOGGER.info("----------- Verifying HTTP Status -----------");
			assertEquals(200, status);
			LOGGER.info("----------- Searching for User -----------");
			User userFound = userService.getUserById(new Long(2));
			LOGGER.info("----------- Verifying User -----------");
			assertEquals(userFound.getUserName(), newUser.getUserName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deleteEntity() {
		MvcResult mvcResult;
		LOGGER.info("----------- Testing deleteEntity Method -----------");
		try {
			LOGGER.info("----------- Constructing and Saving User -----------");
			userService.addUser(new User(2, "Paul"));
			LOGGER.info("----------- Mocking Context Webservice and Invoking the Webservice -----------");
			mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri + "/2")).andReturn();
			LOGGER.info("----------- Getting HTTP Status -----------");
			int status = mvcResult.getResponse().getStatus();
			LOGGER.info("----------- Verifying HTTP Status -----------");
			assertEquals(200, status);
			LOGGER.info("----------- Searching for User -----------");
			User userFound = userService.getUserById(new Long(2));
			LOGGER.info("----------- Verifying User -----------");
			assertEquals(userFound, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* Serialize the given object into Json
	*/
	protected final String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	
	/** 
	* Serialize the given Json into object
	*/
	protected final <T> T mapFromJson(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
	
	
	
}
