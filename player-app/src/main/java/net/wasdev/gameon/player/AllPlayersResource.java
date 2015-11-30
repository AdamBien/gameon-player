/*******************************************************************************
 * Copyright (c) 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package net.wasdev.gameon.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Providers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * All the players, and searching for players.
 *
 */
@Path("/")
public class AllPlayersResource {
	@Context
	HttpServletRequest httpRequest;
	
	@Context Providers ps;

	@Resource(name = "mongo/playerDB")
	protected DB playerDB;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getAllPlayers() throws IOException{
		// TODO: wrap this in a reactive / lazy stream
		DBCollection players = playerDB.getCollection("players");
		DBObject query = null;
		DBCursor cursor = players.find(query);

		List<Player> results = new ArrayList<Player>();
		for(DBObject player : cursor){
			Player p = Player.fromDBObject(ps, player);
			results.add(p);
		}

		return results;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlayer(Player player) throws IOException{
		
		//set by the auth filter.
		String authId = (String) httpRequest.getAttribute("player.id");
		
		//only allow create for matching id.
		if(authId==null || !authId.equals(player.getId())){
			return Response.status(403).entity("Bad authentication id").build();
		}
		
		DBCollection players = playerDB.getCollection("players");
		DBObject query = new BasicDBObject("id",player.getId());
		DBCursor cursor = players.find(query);

		if(cursor.hasNext()){
			return Response.status(409).entity("Error player : "+player.getName()+" already exists").build();
		}				

		System.out.println("player "+player.toString());
		
		System.out.println("ps == null? " + (null == ps));
		DBObject playerToStore = player.toDBObject(ps);
		players.insert(playerToStore);

		return Response.status(201).build();
	}
}
