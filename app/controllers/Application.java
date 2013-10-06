package controllers;

import models.Game;
import models.Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.StandingsCalculator;
import views.html.charts;
import views.html.overview;
import views.html.players;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GameGroupInSession.Validated
@Security.Authenticated(Secured.class)
public class Application extends Controller {

    public static Result overview() {
        DynamicForm form = DynamicForm.form();

        Map<String, String> defaults = new HashMap<String, String>();
        defaults.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        defaults.put("pointsLeft", "11");
        form = form.bind(defaults);

        List<Player> all = Player.all();
        return ok(overview.render(StandingsCalculator.create(Game.all(), Player.all()), form, all));
    }

    public static Result insert() {

        DynamicForm gameForm = DynamicForm.form().bindFromRequest();
        String dateString = gameForm.data().get("date");
        String playerLeftName = gameForm.data().get("playerLeft");
        String playerRightName = gameForm.data().get("playerRight");
        String pointsLeftString = gameForm.data().get("pointsLeft");
        String pointsRightString = gameForm.data().get("pointsRight");

        Date date = null;
        try {
            date = DateUtils.parseDateStrictly(dateString, "yyyy-MM-dd");
        } catch (ParseException e) {
            gameForm.reject("date", "Supplied date is not valid! Use the format 'yyyy-MM-dd'.");
        }

        if (StringUtils.isBlank(playerLeftName)) {
            gameForm.reject("playerLeft", "Left player name must not be empty.");
        }
        if (StringUtils.isBlank(playerRightName)) {
            gameForm.reject("playerRight", "Right player name must not be empty.");
        }
        if (StringUtils.isNotBlank(playerLeftName) && playerLeftName.equals(playerRightName)) {
            gameForm.reject("playerRight", "Right player name must not be same as player left.");
        }
        if (!NumberUtils.isDigits(pointsLeftString)) {
            gameForm.reject("pointsLeft", "Left points must be a valid number.");
        }
        if (!NumberUtils.isDigits(pointsRightString)) {
            gameForm.reject("pointsRight", "Right points must be a valid number.");
        }

        if(gameForm.hasErrors()) {
            return badRequest(overview.render(StandingsCalculator.create(Game.all(), Player.all()), gameForm, Player.all()));
        } else {
            Player player1 = Player.getOrCreate(playerLeftName);
            Player player2 = Player.getOrCreate(playerRightName);
            int pointsLeft = Integer.parseInt(pointsLeftString.trim());
            int pointsRight = Integer.parseInt(pointsRightString.trim());
            Game.insert(date, player1, player2, pointsLeft, pointsRight);
            flash("message", String.format("%s %s %d-%d added", playerLeftName, playerRightName, pointsLeft, pointsRight));
            return redirect(routes.Application.overview());
        }
    }

    public static Result removeGame(Long id) {
        Game.remove(id);
        return redirect(routes.Application.overview());
    }

    public static Result players() {
        DynamicForm form = DynamicForm.form();
        return ok(players.render(Player.all(), form));
    }

    public static Result insertPlayer() {
        DynamicForm playerForm = DynamicForm.form().bindFromRequest();

        String name = playerForm.data().get("name");
        if (StringUtils.isBlank(name)) {
            playerForm.reject("name", "Name must not be empty.");
        }

        if (playerForm.hasErrors()) {
            return badRequest(players.render(Player.all(), playerForm));
        } else {
            Player.getOrCreate(name);
            return redirect(routes.Application.players());
        }
    }

    public static Result removePlayer(long id) {
        Player.remove(id);
        return redirect(routes.Application.players());
    }

    public static Result charts() {
        return ok(charts.render(StandingsCalculator.create(Game.all(), Player.all())));
    }

}
