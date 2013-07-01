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

    public static final double STAKE_PER_GAME = 0.1;

    @Id
    public long id;

    @Constraints.Required
    public Date date;

    @ManyToOne()
    @Constraints.Required
    public Player playerLeft;

    public int beforeLeft;
    public int stakeLeft;
    public int afterLeft;

    @ManyToOne()
    @Constraints.Required
    public Player playerRight;

    public int beforeRight;
    public int stakeRight;
    public int afterRight;

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
        Game game = new Game();
        game.date = date;
        game.playerLeft = playerLeft;
        game.playerRight = playerRight;
        game.pointsLeft = pointsLeft;
        game.pointsRight = pointsRight;

        game.beforeLeft = playerLeft.currentPoints;
        game.stakeLeft = (int) (game.beforeLeft * STAKE_PER_GAME);

        game.beforeRight = playerRight.currentPoints;
        game.stakeRight = (int) (game.beforeRight * STAKE_PER_GAME);

        if (pointsLeft > pointsRight) {
            game.afterLeft = game.beforeLeft + game.stakeRight;
            game.afterRight = game.beforeRight - game.stakeRight;
            playerLeft.addPoints(game.stakeRight);
            playerRight.withdrawPoints(game.stakeRight);
        } else if (pointsRight > pointsLeft) {
            game.afterLeft = game.beforeLeft - game.stakeLeft;
            game.afterRight = game.beforeRight + game.stakeLeft;
            playerLeft.withdrawPoints(game.stakeLeft);
            playerRight.addPoints(game.stakeLeft);
        } else {
            game.afterLeft = game.beforeLeft;
            game.afterRight = game.beforeRight;
        }

        game.save();
        return game;
    }

}
