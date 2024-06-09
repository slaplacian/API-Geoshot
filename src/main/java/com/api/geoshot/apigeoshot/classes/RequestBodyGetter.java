package com.api.geoshot.apigeoshot.classes;

import jakarta.servlet.http.HttpServletRequest;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestBodyGetter {
    public static JsonObject getJsonBody(HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = request.getReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

        String json = stringBuilder.toString();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        return jsonObject;
    }
}
