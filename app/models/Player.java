package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Player extends Model {

    @Id
    public long id;

    @Constraints.Required
    @Column(unique = true)
    public String name;

    @SuppressWarnings("unchecked")
    public static Finder<Long, Player> find = new Finder(Long.class, Player.class);

    public static List<Player> all() {
        return find.where().orderBy("name").findList();
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
            newPlayer.save();
            return newPlayer;
        }
    }

}
