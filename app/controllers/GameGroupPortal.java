package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.gameGroups;

public class GameGroupPortal extends Controller {

    public static Result gameGroups() {
        return ok(gameGroups.render(models.GameGroup.all()));
    }
}
