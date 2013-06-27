package controllers;

import play.*;
import play.mvc.*;

import views.html.login;
import views.html.overview;

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
        return ok(overview.render());
    }

    public static Result insert() {
        return redirect(routes.Application.overview());
    }
}
