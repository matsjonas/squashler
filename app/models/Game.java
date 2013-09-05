package models;

import com.avaje.ebean.Expr;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Entity
public class Game extends Model {

    @Id
    public long id;

    @Constraints.Required
    public Date date;

    public int gameNbr;

    @ManyToOne()
    @Constraints.Required
    public Player playerLeft;

    @ManyToOne()
    @Constraints.Required
    public Player playerRight;

    @Constraints.Required
    public int pointsLeft;

    @Constraints.Required
    public int pointsRight;

    @SuppressWarnings("unchecked")
    public static Finder<Long, Game> find = new Finder(Long.class, Game.class);

    public static List<Game> all() {
        return find.where().orderBy("date, gameNbr").findList();
    }

    public static Game byId(long id) {
        return find.byId(id);
    }

    public static Game insert(Game game) {
        game.save();
        return game;
    }

    public static void remove(Long id) {
        Game gameToRemove = find.byId(id);
        int oldIndex = gameToRemove.gameNbr;
        gameToRemove.delete();
        List<Game> games = find.where().gt("gameNbr", oldIndex).findList();
        for (Game game : games) {
            game.gameNbr--;
            game.save();
        }
    }

    public static Game insert(Date date, Player playerLeft, Player playerRight, int pointsLeft, int pointsRight) {
        Game game = new Game();
        update(game, date, playerLeft, playerRight, pointsRight, pointsLeft);
        return game;
    }

    public static void update(Game game, Date date, Player playerLeft, Player playerRight, int pointsRight, int pointsLeft) {
        game.date = date;
        game.gameNbr = getMaxGameNbr(date) + 1;
        game.playerLeft = playerLeft;
        game.playerRight = playerRight;
        game.pointsLeft = pointsLeft;
        game.pointsRight = pointsRight;

        game.save();
        recalculateGameNbrsFrom(game);
    }

    private static void recalculateGameNbrsFrom(Game game) {
        List<Game> list = find.where()
                .or(Expr.gt("date", game.date), Expr.ge("gameNbr", game.gameNbr))
                .orderBy("date, gameNbr").findList();
        int gameNbr = game.gameNbr;
        for (Game currentGame : list) {
            if (currentGame.id != game.id) {
                currentGame.gameNbr = ++gameNbr;
                currentGame.save();
            }
        }
    }

    private static int getMaxGameNbr(Date date) {
        List<Game> list = find.where().le("date", date).orderBy("gameNbr desc").setMaxRows(1).findList();
        if (list.isEmpty()) {
            return 0;
        } else {
            return list.get(0).gameNbr;
        }
    }

}
