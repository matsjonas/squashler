package models;

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
        return find.where().orderBy("date").findList();
    }

    public static Game byId(Long id) {
        return find.ref(id);
    }

    public static Game insert(Game game) {
        game.save();
        return game;
    }

    public static Game insert(Date date, Player playerLeft, Player playerRight, int pointsLeft, int pointsRight) {
        Game newGame = new Game();
        newGame.date = date;
        newGame.playerLeft = playerLeft;
        newGame.playerRight = playerRight;
        newGame.pointsLeft = pointsLeft;
        newGame.pointsRight = pointsRight;
        newGame.save();
        return newGame;
    }

}
