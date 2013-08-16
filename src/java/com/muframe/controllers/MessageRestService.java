package com.muframe.controllers;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.muframe.server.MuServer;

@Path("/showPhoto")
public class MessageRestService {

	@POST
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String photoId) {
		System.out.println("/showPhoto/x called");
		MuServer.showPhoto(photoId); //complete hack.. ohh my god. FIXME
		return Response.status(200).build();
//		return Response.status(200).entity(shared).build();
	}
}
