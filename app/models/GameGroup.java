package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class GameGroup extends Model {

    @Id
    public long id;

    @Constraints.Required
    public String name;

    @OneToMany(mappedBy = "gameGroup")
    public List<Game> games;

    @SuppressWarnings("unchecked")
    public static Finder<Long, GameGroup> find = new Finder(Long.class, GameGroup.class);

    public static List<GameGroup> all() {
        return find.orderBy("id").findList();
    }

    public static GameGroup byId(long id) {
        return find.byId(id);
    }

    public static GameGroup insert(GameGroup gameGroup) {
        gameGroup.save();
        return gameGroup;
    }

    public static void remove(Long id) {
        GameGroup toRemove = byId(id);
        toRemove.delete();
    }

    public static GameGroup insert(String name) {
        GameGroup gameGroup = new GameGroup();
        gameGroup.name = name;
        gameGroup.save();
        return gameGroup;
    }

}
