package models;

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

    public Date date;

    @ManyToOne()
    public Player playerLeft;

    @ManyToOne()
    public Player playerRight;

    public int pointsLeft;

    public int pointsRight;

    @SuppressWarnings("unchecked")
    public static Finder<Long, Game> find = new Finder(Long.class, Game.class);

    public static List<Game> all() {
        return find.where().orderBy("date").findList();
    }

    public static Game byId(Long id) {
        return find.ref(id);
    }

    public static Game insert(Player playerLeft, Player playerRight, int pointsLeft, int pointsRight) {
        Game newGame = new Game();
        newGame.playerLeft = playerLeft;
        newGame.playerRight = playerRight;
        newGame.pointsLeft = pointsLeft;
        newGame.pointsRight = pointsRight;
        newGame.save();
        return newGame;
    }

}
