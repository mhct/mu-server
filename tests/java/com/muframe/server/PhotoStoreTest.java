package com.muframe.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhotoStoreTest {

	
	@Test
	public void testInitializeDatabase() {
//		PhotoStore.dropStore();
		PhotoStore.initializeStore(); //WARNING creates state at the database
	}

	@Test
	public void testGetInstance() {
		
	}

	@Test
	public void testAddPhotoId() {
		PhotoStore.initializeStore();
		PhotoStore store = PhotoStore.getInstance();
		
		String testData = "this is a test";
		store.addPhotoId(testData);
		
		assertEquals(testData, store.getLastPhotoId());
	}
}
