package controllers;

import models.Game;
import models.Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import play.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result index() {
        return redirect(routes.Application.login());
    }

    public static Result login() {
        return ok(login.render(form(LoginCredentials.class)));
    }

    public static Result authenticate() {
        Form<LoginCredentials> loginForm = form(LoginCredentials.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session(Secured.COOKIE_SECURITY_KEY, Crypto.encryptAES(loginForm.get().username));
            return redirect(routes.Application.overview());
        }
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Application.login());
    }

    @Security.Authenticated(Secured.class)
    public static Result overview() {
        return ok(overview.render(Player.all(), Game.all(), gameForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result insert() {

        DynamicForm filledForm = gameForm.bindFromRequest();
        String dateString = (String) filledForm.data().get("date");
        String playerLeftName = (String) filledForm.data().get("playerLeft");
        String playerRightName = (String) filledForm.data().get("playerRight");
        String pointsLeftString = (String) filledForm.data().get("pointsLeft");
        String pointsRightString = (String) filledForm.data().get("pointsRight");

        Date date = null;
        try {
            date = DateUtils.parseDateStrictly(dateString, "yyyy-MM-dd");
        } catch (ParseException e) {
            filledForm.reject("date", "Supplied date is not valid! Use the format 'yyyy-MM-dd'.");
        }

        if (StringUtils.isBlank(playerLeftName)) {
            filledForm.reject("playerLeft", "Left player name must not be empty.");
        }
        if (StringUtils.isBlank(playerRightName)) {
            filledForm.reject("playerRight", "Right player name must not be empty.");
        }
        if (!NumberUtils.isDigits(pointsLeftString)) {
            filledForm.reject("pointsLeft", "Left points must be a valid number.");
        }
        if (!NumberUtils.isDigits(pointsRightString)) {
            filledForm.reject("pointsRight", "Right points must be a valid number.");
        }

        if(filledForm.hasErrors()) {
            return badRequest(overview.render(Player.all(), Game.all(), filledForm));
        } else {
            Player player1 = Player.getOrCreate(playerLeftName);
            Player player2 = Player.getOrCreate(playerRightName);
            int pointsLeft = Integer.parseInt(pointsLeftString.trim());
            int pointsRight = Integer.parseInt(pointsRightString.trim());
            Game.insert(date, player1, player2, pointsLeft, pointsRight);
            return redirect(routes.Application.overview());
        }
    }

    static DynamicForm gameForm = DynamicForm.form();

    public static class LoginCredentials {

        public String username;
        public String password;

        public String validate() {
            if(!username.equals("admin") || !password.equals("monkeyballs")) {
                return "Invalid username or password";
            }
            return null;
        }

    }

}
