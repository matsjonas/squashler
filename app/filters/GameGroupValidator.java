package filters;

import controllers.routes;
import models.GameGroup;
import play.libs.F;
import play.mvc.*;
import util.Constants;

public class GameGroupValidator extends Action<RequiresGameGroup> {

    public F.Promise<SimpleResult> call(Http.Context ctx) {
        try {
            GameGroup gameGroup = null;

            if (ctx.session().containsKey(Constants.SESSION_KEY_GAME_GROUP)) {
                long gameGroupId = Long.parseLong(ctx.session().get(Constants.SESSION_KEY_GAME_GROUP));
                gameGroup = GameGroup.byId(gameGroupId);
            }

            if (gameGroup == null) {
                Result redirect = Results.redirect(routes.GameGroupPortal.gameGroups());
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
