package models;

import org.apache.commons.lang3.StringUtils;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Player extends Model {

    @Id
    public long id;

    @Constraints.Required
    @Constraints.MinLength(1)
    @Column(unique = true)
    public String name;

    @OneToMany(mappedBy = "playerLeft")
    public List<Game> gamesOnLeft;

    @OneToMany(mappedBy = "playerRight")
    public List<Game> gamesOnRight;

    @SuppressWarnings("unchecked")
    public static Finder<Long, Player> find = new Finder(Long.class, Player.class);

    public static List<Player> all() {
        return find.where().orderBy("name").findList();
    }

    public static Player byName(String name) {
        return find.where().eq("name", name).orderBy("name").findUnique();
    }

    public static List<Player> search(String query) {
        return find.where().ilike("name", String.format("%%%s%%", query)).findList();
    }

    public static Player getOrCreate(String name) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("Supplied name must not be null!");
        }
        name = String.format("%s%s", name.substring(0, 1).toUpperCase(), name.substring(1));
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
