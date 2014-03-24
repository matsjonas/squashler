package controllers;

import models.Game;
import models.GameGroup;
import models.Player;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import util.JsonUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class RestfulAPI extends Controller {

    public static Result gameGroups() {
        ArrayNode result = JsonUtils.newArrayNode();
        for (GameGroup gameGroup : GameGroup.all()) {
            result.add(getGameGroupJsonNode(gameGroup));
        }
        return ok(result);
    }

    public static Result createGameGroup() {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();
        String name = request.get("name").asText();
        GameGroup gameGroup = GameGroup.insert(name);
        return ok(getGameGroupJsonNode(gameGroup));
    }

    public static Result gameGroup(long id) {
        GameGroup gameGroup = GameGroup.byId(id);
        return ok(getGameGroupJsonNode(gameGroup));
    }

    public static Result updateGameGroup(long id) {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();
        String name = request.get("name").asText();
        GameGroup gameGroup = GameGroup.byId(id);
        gameGroup.name = name;
        gameGroup.save();
        return ok(getGameGroupJsonNode(gameGroup));
    }

    public static Result deleteGameGroup(long id) {
        GameGroup.remove(id);
        return ok();
    }

    public static Result players() {
        ArrayNode result = JsonUtils.newArrayNode();
        for (Player player : Player.all()) {
            result.add(getPlayerJsonNode(player));
        }
        return ok(result);
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

    public static Result player(long id) {
        Player player = Player.byId(id);
        return ok(getPlayerJsonNode(player));
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

    public static Result games(long gameGroupId) {
        GameGroup gameGroup = GameGroup.byId(gameGroupId);
        List<Game> games = Game.allInGroup(gameGroup.id);
        ArrayNode result = JsonUtils.newArrayNode();
        for (Game game : games) {
            result.add(getGameJsonNode(game));
        }
        return ok(result);
    }

    public static Result createGame(long gameGroupId) {
        Http.RequestBody body = request().body();
        JsonNode request = body.asJson();

        Date date = new Date();
        if (request.has("date")) {
            try {
                date = DateUtils.parseDate(request.get("date").asText(), "yyyy-MM-dd");

            } catch (ParseException ignored) {
            }
        }

        if (!request.has("playerLeft") || !request.has("playerRight")) {
            return badRequest("No players specified");
        }
        if (!request.has("pointsLeft") || !request.has("pointsRight")) {
            return badRequest("No points specified");
        }

        Player playerLeft = Player.byId(request.get("playerLeft").asInt());
        Player playerRight = Player.byId(request.get("playerRight").asInt());
        int pointsLeft = request.get("pointsLeft").asInt();
        int pointsRight = request.get("pointsRight").asInt();
        GameGroup gameGroup = GameGroup.byId(gameGroupId);

        if (playerLeft == null || playerRight == null) {
            return badRequest("Invalid players");
        }
        if (gameGroup == null) {
            return badRequest("Invalid game group");
        }

        Game game = Game.insert(date, playerLeft, playerRight, pointsLeft, pointsRight, gameGroup);

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        result.put("game", getGameJsonNode(game));
        return ok(result);
    }

    public static Result game(long gameGroupId, long id) {
        Game game = Game.byId(id);
        if (game.gameGroup.id != gameGroupId) {
            return badRequest("Game and group mismatch");
        }
        return ok(getGameJsonNode(game));
    }

    public static Result updateGame(long gameGroupId, long id) {
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
        if (game.gameGroup.id != gameGroupId) {
            return badRequest("Game and group mismatch");
        }
        Game.update(game, date, playerLeft, playerRight, pointsLeft, pointsRight);

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        result.put("game", getGameJsonNode(game));
        return ok(result);
    }

    public static Result deleteGame(long gameGroupId, long id) {
        Game game = Game.byId(id);
        if (game.gameGroup.id != gameGroupId) {
            return badRequest("Game and group mismatch");
        }
        game.delete();

        ObjectNode result = JsonUtils.newObjectNode();
        result.put("status", "OK");
        return ok(result);
    }

    private static ObjectNode getGameGroupJsonNode(GameGroup gameGroup) {
        ObjectNode result = JsonUtils.newObjectNode();
        result.put("id", gameGroup.id);
        result.put("name", gameGroup.name);
        return result;
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
        node.put("gameGroupId", game.gameGroup.id);
        node.put("date", DateFormatUtils.format(game.date, "yyyy-MM-dd HH:mm"));
        node.put("gameNbr", game.gameNbr);
        node.put("playerLeft", getPlayerJsonNode(game.playerLeft));
        node.put("playerRight", getPlayerJsonNode(game.playerRight));
        node.put("pointsLeft", game.pointsLeft);
        node.put("pointsRight", game.pointsRight);
        return node;
    }
}
