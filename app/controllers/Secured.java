package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

    public static final String COOKIE_SECURITY_KEY = "SECKEY";

    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get(COOKIE_SECURITY_KEY);
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect(routes.Login.login());
    }

}
