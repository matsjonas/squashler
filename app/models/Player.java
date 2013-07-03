package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Player extends Model {

    public static final int STARTING_POINTS = 1000;

    @Id
    public long id;

    @Constraints.Required
    @Column(unique = true)
    public String name;

    public int currentPoints;

    public int nbrGames;

    public int wins;

    public int losses;

    @SuppressWarnings("unchecked")
    public static Finder<Long, Player> find = new Finder(Long.class, Player.class);

    public static List<Player> all() {
        return find.where().orderBy("currentPoints desc").findList();
    }

    public static Player byId(Long id) {
        return find.ref(id);
    }

    public static Player byName(String name) {
        return find.where().eq("name", name).orderBy("name").findUnique();
    }

    public static Player getOrCreate(String name) {
        Player existingPlayer = byName(name);
        if (existingPlayer != null) {
            return existingPlayer;
        } else {
            Player newPlayer = new Player();
            newPlayer.name = name;
            newPlayer.currentPoints = STARTING_POINTS;
            newPlayer.nbrGames = 0;
            newPlayer.wins = 0;
            newPlayer.losses = 0;
            newPlayer.save();
            return newPlayer;
        }
    }

    public void addPoints(int points) {
        currentPoints += points;
        this.save();
    }

    public void withdrawPoints(int points) {
        currentPoints -= points;
        this.save();
    }

}
