package controllers;

import models.GameGroup;
import play.libs.F;
import play.mvc.*;
import play.mvc.Http.*;
import util.Constants;

import java.lang.annotation.*;

public class GameGroupInSession {

    @With(GameGroupInSessionAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Validated {

    }

    public static class GameGroupInSessionAction extends Action<Validated> {

        public F.Promise<SimpleResult> call(Context ctx) {
            try {
                long gameGroupId = Long.parseLong(ctx.session().get(Constants.SESSION_KEY_GAME_GROUP));
                GameGroup gameGroup = GameGroup.byId(gameGroupId);

                if(gameGroup == null) {
                    Result redirect = redirect(routes.GameGroupPortal.gameGroups());
                    return F.Promise.pure((SimpleResult) redirect);
                } else {
                    return delegate.call(ctx);
                }
            } catch(RuntimeException e) {
                throw e;
            } catch(Throwable t) {
                throw new RuntimeException(t);
            }
        }

    }

}
