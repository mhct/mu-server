package com.muframe.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PhotoStoreTest {

	@Before
	public void initialize() {
		PhotoStore.initializeStore();
	}
	
//	@Test
//	public void testInitializeDatabase() {
////		PhotoStore.dropStore();
//		PhotoStore.initializeStore(); //WARNING creates state at the database
//	}

	@Test
	public void testGetInstance() {
		PhotoStore store = PhotoStore.getInstance();
		assertNotNull(store);
	}

	@Test
	public void testAddPhotoId() {
		PhotoStore store = PhotoStore.getInstance();
		
		String testData = "photo1.jpg";
		store.addPhotoId(testData);
		
		assertEquals(testData, store.getLastPhotoId());
	}
	
	@Test
	public void testGetRandomPhotoId() {
		PhotoStore store = PhotoStore.getInstance();
		
		store.addPhotoId("photo0.jpg");
		store.addPhotoId("photo1.jpg");
		store.addPhotoId("photo2.jpg");
		store.addPhotoId("photo3.jpg");
		store.addPhotoId("photo4.jpg");
		store.addPhotoId("photo5.jpg");
		store.addPhotoId("photo6.jpg");
		store.addPhotoId("photo7.jpg");
		store.addPhotoId("photo8.jpg");
		store.addPhotoId("photo9.jpg");

		int photo5Selected = 0;
		for (int i=0; i<100; i++) {
			if (store.getRandomPhotoId().equals("photo5.jpg")) {
				photo5Selected++;
			}
		}

		// Not really testing the randomness of the generated numbers here. simply testing if the function getRandomPhotoId returns something...
		// which is not 100% the same result.
		assertTrue(photo5Selected >= 5);
		assertTrue(photo5Selected <= 50);
	}
}
