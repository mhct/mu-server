package com.muframe.server;

import java.util.UUID;

public class UUIDGenerator implements IDStrategy {

	@Override
	public String getId() {
		return UUID.randomUUID().toString();
	}
	
	public static UUIDGenerator getInstance() {
		return new UUIDGenerator();
	}

}
