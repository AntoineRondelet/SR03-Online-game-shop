package com.gameshop.webservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pierrelouislacorte on 02/05/2017.
 */
public class webserviceUtil {

    /* ------- CreateSessionQuery
     * This function aim to create appropriate request depending on parameters
     * - input : Name of the Database you want to access and Map of (property, name of the parameter)
     * - output : Request as a string
     *
     * Note : This function works for Console but does not seem to work for others.
     */
    public static String createSessionQuery (String Class, HashMap<String,String> parameters) {
        String query = "from " + Class;
        Boolean firstParam = true;
        if (!parameters.isEmpty()) {
            query = query + " g where";
            //pour chaque paramètre
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String cle = entry.getKey();
                String valeur = entry.getValue();

                // traitements quand le parametre n'est pas vide
                if (valeur != null) {
                    //cas du premier parametre (ne pas ajouter le "and" devant)
                    if (firstParam) {
                        query = query + " g." + cle + " = :" + cle + Class;
                        firstParam = false;
                    } else {
                        query = query + " and g." + cle + " = :" + cle + Class;
                    }
                } else {
                    // traitements quand le parametre est vide
                    if (firstParam) {
                        query = query + " :" + cle + Class + " is null";
                        firstParam = false;
                    } else {
                        query = query + " and :" + cle + Class + " is null";
                    }
                }
            }
        }
        return query;
    }
}
