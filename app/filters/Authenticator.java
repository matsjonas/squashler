package filters;

import controllers.routes;
import play.libs.F;
import play.mvc.*;
import util.Constants;

public class Authenticator extends Action<RequiresAuthentication> {

    public F.Promise<SimpleResult> call(Http.Context ctx) {
        try {
            String securityKey = ctx.session().get(Constants.SESSION_KEY_SECURITY);

            if (securityKey == null) {
                Result redirect = Results.redirect(routes.Login.login());
                return F.Promise.pure((SimpleResult) redirect);
            } else {
                return delegate.call(ctx);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
