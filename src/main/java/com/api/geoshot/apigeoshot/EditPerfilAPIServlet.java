package com.api.geoshot.apigeoshot;

// import com.api.geoshot.apigeoshot.classes.PhotoConverter;
import com.api.geoshot.apigeoshot.classes.HashGeneretor;
import com.api.geoshot.apigeoshot.classes.RequestBodyGetter;
import com.api.geoshot.apigeoshot.classes.User;
import com.api.geoshot.apigeoshot.classes.usersDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
// import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
// import java.io.InputStream;


@WebServlet(name="edit-perfil", value="/api/edit-perfil")
public class EditPerfilAPIServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if(username.isEmpty()) {

            jsonResponse.addProperty("status","error");

        } else {

            jsonResponse.addProperty("status","success");
            jsonResponse.addProperty("username",username);
            usersDAO UserManager = new usersDAO();
            User thisUser = UserManager.getUser(username);

            Gson gson = new Gson();

            jsonResponse.addProperty("user",gson.toJson(thisUser));

        }

        response.getWriter().write(jsonResponse.toString());

    }

    public  void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JsonObject body = RequestBodyGetter.getJsonBody(request);

        String username = body.get("username").getAsString();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        Gson gson = new Gson();
        User thisUser;

        if(username == null) {

            jsonResponse.addProperty("status","error");

        } else {
            usersDAO UserManager = new usersDAO();
            thisUser = UserManager.getUser(username);
            String oldPassword = body.get("old-password").getAsString();
            String newPassword = body.get("password").getAsString();
            String newConfirmPassword = body.get("confirm-password").getAsString();

            // mesmo que CREATECHALL

            String encodedPhoto = body.get("photo").getAsString();

            boolean noMatchNewPassword    = false;
            boolean noPhotoOrPassword     = false;
            boolean noOldPasswordOnUpdate = false;

            String oldPasswordFromBD = UserManager.getUser(username).getPassword();

            if((oldPassword.isEmpty() || !HashGeneretor.getHash("SHA256",oldPassword).equals(oldPasswordFromBD)) && !newPassword.isEmpty()) {
                noOldPasswordOnUpdate = true;
            }

            if(!newPassword.equals(newConfirmPassword)) {
                noMatchNewPassword = true;
            }

            if(newPassword.isEmpty() && encodedPhoto.isEmpty()) {
                noPhotoOrPassword = true;
            }

            if(noPhotoOrPassword || noMatchNewPassword || noOldPasswordOnUpdate) {
                jsonResponse.addProperty("status","error");
                jsonResponse.addProperty("username",username);
                jsonResponse.addProperty("user",gson.toJson(thisUser));
                jsonResponse.addProperty("no-photo",noPhotoOrPassword?"true":"false");
                jsonResponse.addProperty("non-equal-password",noMatchNewPassword?"true":"false");
                jsonResponse.addProperty("no-old-password-on-update",noOldPasswordOnUpdate?"true":"false");
            } else {

                jsonResponse.addProperty("status","success");
                jsonResponse.addProperty("username",username);
                jsonResponse.addProperty("user",gson.toJson(thisUser));
                jsonResponse.addProperty("no-photo","false");
                jsonResponse.addProperty("non-equal-password","false");
                jsonResponse.addProperty("no-old-password-on-update","false");

                if(!newPassword.isEmpty()) {
                    // Editar Apenas Senha;
                    UserManager.changePasswordFromUser(username,newPassword);

                }

                if(!encodedPhoto.isEmpty()) {
                    // Editar Apenas photo;
                    UserManager.changePhotoFromUser(username,encodedPhoto);
                }

            }


        }
        response.getWriter().write(jsonResponse.toString());
    }

}

