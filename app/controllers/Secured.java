package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.Constants;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get(Constants.SESSION_KEY_SECURITY);
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect(routes.Login.login());
    }

}
