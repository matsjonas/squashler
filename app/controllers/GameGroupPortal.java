package controllers;

import models.Game;
import models.GameGroup;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.gameGroups;

import java.util.List;

@Security.Authenticated(Secured.class)
public class GameGroupPortal extends Controller {

    public static Result gameGroups() {
        return ok(gameGroups.render(GameGroup.all(), DynamicForm.form()));
    }

    public static Result insertGameGroup() {
        DynamicForm playerForm = DynamicForm.form().bindFromRequest();

        String name = playerForm.data().get("name");
        if (StringUtils.isBlank(name)) {
            playerForm.reject("name", "Name must not be empty.");
        }

        if (playerForm.hasErrors()) {
            return badRequest(gameGroups.render(GameGroup.all(), playerForm));
        } else {
            GameGroup gameGroup = GameGroup.insert(name);
            if (GameGroup.all().size() == 1) {
                List<Game> games = Game.all();
                for (Game game : games) {
                    game.gameGroup = gameGroup;
                    game.save();
                }
            }
            return redirect(routes.GameGroupPortal.gameGroups());
        }
    }

    public static Result selectGameGroup(long gamegroupid) {
        session().put(GameGroupInSession.GAMEGROUP_SESSION_KEY, String.valueOf(gamegroupid));

        return redirect(routes.Application.overview());
    }

}
