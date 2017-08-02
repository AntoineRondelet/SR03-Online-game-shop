package com.gameshop.webservice;

import com.gameshop.model.GameEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import java.util.List;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

@Secured
@Path("/gameEntity")
public class GameEntityPath {
    @GET
    @Produces("application/json")
    @Path("/")
    // This function returns the list of all game entities in the store //
    public String getGameEntities() {
        // DB management
        SessionFactory sfactory = getSessionFactory();
        Session session = sfactory.openSession();
        List gameEntities = session.createQuery("from GameEntity ").getResultList();

        // JSON response
        JsonObjectBuilder jsonResult = Json.createObjectBuilder();

        // JSON Array of all the game entities
        JsonArrayBuilder listBuilder = Json.createArrayBuilder();
        for (int i = 0; i < gameEntities.size(); i++) {
            GameEntity gres = (GameEntity) gameEntities.get(i);
            listBuilder.add(Json.createObjectBuilder()
                    .add("id", gres.getId())
                    .add("title", gres.getIdGame()));
        }

        // Add the JSON Array to the JSON response
        jsonResult.add("list", listBuilder.build());

        //sfactory.close();

        // Return the JSON response as a string
        return jsonResult.build().toString();
    }

    @GET
    @Produces("application/json")
    @Path("/query")
    // This function returns the list of all console in the store //
    public String getConsole(@QueryParam("id") String idGameEntity,
                             @QueryParam("title") String titleGameEntity
    ) {
        //create session
        SessionFactory sfactory = getSessionFactory();
        Session session = sfactory.openSession();

        //create appropriate request
        String hql = "from GameEntity g where " +
                "(g.id =:idGameEntity or :idGameEntity is null)" +
                "and (g.idGame =:titleGameEntity or :titleGameEntity is null)";

        //add parameters to request
        List consoles = session.createQuery(hql)
                .setParameter("idGameEntity",idGameEntity)
                .setParameter("titleGameEntity", titleGameEntity)
                .getResultList();

        // JSON response
        JsonObjectBuilder jsonResult = Json.createObjectBuilder();

        JsonObjectBuilder jsonConsole = Json.createObjectBuilder();
        for (int i = 0; i < consoles.size(); i++) {
            GameEntity gres = (GameEntity) consoles.get(i);
            jsonResult.add("Console"+i,
                    jsonConsole
                            .add("id", gres.getId())
                            .add("title", gres.getIdGame())
                            .build().toString());
        }

        //sfactory.close();

        return jsonResult.build().toString();
    }


}
