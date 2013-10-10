package controllers;

import play.libs.F;
import play.mvc.*;
import play.mvc.Http.*;

import java.lang.annotation.*;

public class GameGroupInSession {

    public final static String GAMEGROUP_SESSION_KEY = "gameGroup";

    @With(GameGroupInSessionAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Validated {

    }

    public static class GameGroupInSessionAction extends Action<Validated> {

        public F.Promise<SimpleResult> call(Context ctx) {
            try {
                String gameGroup = ctx.session().get(GAMEGROUP_SESSION_KEY);

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
