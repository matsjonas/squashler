package controllers;

import models.Game;
import models.Player;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Application extends Controller {

    public static Result index() {
        return redirect(routes.Application.login());
    }

    public static Result login() {
        return ok(login.render());
    }

    public static Result loginUser() {
        return redirect(routes.Application.overview());
    }

    public static Result overview() {
        return ok(overview.render(Game.all(), gameForm));
    }

    public static Result insert() {

        DynamicForm filledForm = gameForm.bindFromRequest();
        String dateString = (String) filledForm.data().get("date");
        String playerLeftName = (String) filledForm.data().get("playerLeft");
        String playerRightName = (String) filledForm.data().get("playerRight");
        String pointsLeftString = (String) filledForm.data().get("pointsLeft");
        String pointsRightString = (String) filledForm.data().get("pointsRight");

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parser.parse(dateString);
        } catch (ParseException e) {
            Logger.error(String.format("PARSE ERROR OF DOOM!"), e);
        }
        Player player1 = Player.getOrCreate(playerLeftName);
        Player player2 = Player.getOrCreate(playerRightName);
        int pointsLeft = Integer.parseInt(pointsLeftString);
        int pointsRight = Integer.parseInt(pointsRightString);

        if(filledForm.hasErrors()) {
            return badRequest(overview.render(Game.all(), filledForm));
        } else {
            Game.insert(date, player1, player2, pointsLeft, pointsRight);
            return redirect(routes.Application.overview());
        }
    }

    static DynamicForm gameForm = DynamicForm.form();

}
