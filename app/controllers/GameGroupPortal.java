package controllers;

import models.GameGroup;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.gameGroups;

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
            System.out.println("GameGroupPortal.insertGameGroup 1");
            return badRequest(gameGroups.render(GameGroup.all(), playerForm));
        } else {
            GameGroup.insert(name);
            System.out.println("GameGroupPortal.insertGameGroup 2");
            return redirect(routes.GameGroupPortal.gameGroups());
        }
    }

    public static Result selectGameGroup(long gamegroupid) {
        session().put(GameGroupInSession.GAMEGROUP_SESSION_KEY, String.valueOf(gamegroupid));

        return redirect(routes.Application.overview());
    }

}
