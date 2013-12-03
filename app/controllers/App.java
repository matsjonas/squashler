package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.app;

public class App extends Controller {

    public static Result start() {
        return ok(app.render());
    }

}
