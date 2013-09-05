package controllers;

import models.Game;
import models.Player;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.JsonUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class RestfulAPI extends Controller {

    public static Result player(long id) {
        Player player = Player.byId(id);
        return ok(getPlayerJsonNode(player));
    }

    public static Result createPlayer() {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();

        String name = request.get("name").asText();
        Player player = Player.getOrCreate(name);

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        result.put("player", getPlayerJsonNode(player));
        return ok(result);
    }

    public static Result updatePlayer(long id) {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();

        String name = request.get("name").asText();
        Player player = Player.byId(id);
        player.name = name;
        player.save();

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        result.put("player", getPlayerJsonNode(player));
        return ok(result);
    }

    public static Result deletePlayer(long id) {
        Player player = Player.byId(id);
        player.delete();

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        return ok(result);
    }

    public static Result game(long id) {
        Game game = Game.byId(id);
        return ok(getGameJsonNode(game));
    }

    public static Result createGame() {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();

        Date date = new Date();
        try {
            date = DateUtils.parseDate(request.get("date").asText(), "yyyy-MM-dd");
        } catch (ParseException ignored) { }

        Player playerLeft = Player.byId(request.get("playerLeft").asInt());
        Player playerRight = Player.byId(request.get("playerRight").asInt());
        int pointsLeft = request.get("pointsLeft").asInt();
        int pointsRight = request.get("pointsRight").asInt();

        Game game = Game.insert(date, playerLeft, playerRight, pointsLeft, pointsRight);

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        result.put("game", getGameJsonNode(game));
        return ok(result);
    }

    public static Result updateGame(long id) {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();

        Date date = new Date();
        try {
            date = DateUtils.parseDate(request.get("date").asText(), "yyyy-MM-dd");
        } catch (ParseException ignored) {
        }

        Player playerLeft = Player.byId(request.get("playerLeft").asInt());
        Player playerRight = Player.byId(request.get("playerRight").asInt());
        int pointsLeft = request.get("pointsLeft").asInt();
        int pointsRight = request.get("pointsRight").asInt();

        Game game = Game.byId(id);
        Game.update(game, date, playerLeft, playerRight, pointsLeft, pointsRight);

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        result.put("game", getGameJsonNode(game));
        return ok(result);
    }

    public static Result deleteGame(long id) {
        Game game = Game.byId(id);
        game.delete();

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        return ok(result);
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
