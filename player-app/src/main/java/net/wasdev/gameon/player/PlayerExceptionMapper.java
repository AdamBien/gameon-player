package net.wasdev.gameon.player;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PlayerExceptionMapper implements ExceptionMapper<PlayerNotFoundException> {

	@Override
	public Response toResponse(PlayerNotFoundException exception) {
		return Response.status(404).entity("Player not found").type("text/plain").build();
	}

}
