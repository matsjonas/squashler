package controllers;

import models.Game;
import models.Player;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.mvc.Controller;
import play.mvc.Result;
import util.JsonUtils;

import java.util.List;

public class RestfulAPI extends Controller {

    public static Result player(long id) {
        Player player = Player.byId(id);
        return ok(getPlayerJsonNode(player));
    }

    public static Result createPlayer() {
        return play.mvc.Results.TODO;
    }

    public static Result updatePlayer(long id) {
        return play.mvc.Results.TODO;
    }

    public static Result deletePlayer(long id) {
        return play.mvc.Results.TODO;
    }

    public static Result game(long id) {
        return play.mvc.Results.TODO;
    }

    public static Result createGame() {
        return play.mvc.Results.TODO;
    }

    public static Result updateGame(long id) {
        return play.mvc.Results.TODO;
    }

    public static Result deleteGame(long id) {
        return play.mvc.Results.TODO;
    }

    public static Result games() {
        List<Game> games = Game.all();
        ArrayNode result = JsonUtils.newArrayNode();
        for (Game game : games) {
            result.add(getGameJsonNode(game));
        }
        return ok(result);
    }

    private static ObjectNode getPlayerJsonNode(Player player) {
        ObjectNode result = JsonUtils.newObjectNode();
        result.put("id", player.id);
        result.put("name", player.name);
        return result;
    }

    private static ObjectNode getGameJsonNode(Game game) {
        ObjectNode node = JsonUtils.newObjectNode();
        node.put("id", game.id);
        node.put("date", DateFormatUtils.format(game.date, "yyyy-MM-dd HH:mm"));
        node.put("gameNbr", game.gameNbr);
        node.put("playerLeft", getPlayerJsonNode(game.playerLeft));
        node.put("playerRight", getPlayerJsonNode(game.playerRight));
        node.put("pointsLeft", game.pointsLeft);
        node.put("pointsRight", game.pointsRight);
        return node;
    }

}
