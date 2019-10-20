package com.inti.formation.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MathServiceTest {

	@Autowired
	private MathService mathService;
	
	@Test
	public void additionTest() {
		assertEquals(11, mathService.addition(5, 6));
		assertEquals(7, mathService.addition(4, 3));
	}
	
}
