package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Game;
import models.Player;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

public class StandingsCalculator {

    private static final int STARTING_POINTS = 1000;

    private static final double STAKE_PER_GAME = 0.1;

    List<GameWrapper> games;

    List<PlayerWrapper> players;

    public static StandingsCalculator create(List<Game> games, List<Player> players) {
        return new StandingsCalculator(games, players);
    }

    private StandingsCalculator(List<Game> games, List<Player> players) {
        this.games = new ArrayList<GameWrapper>();
        this.players = new ArrayList<PlayerWrapper>();
        for (Game game : games) {
            this.games.add(new GameWrapper(game));
        }
        for (Player player : players) {
            this.players.add(new PlayerWrapper(player));
        }
        calculateStandings();
    }

    private void calculateStandings() {
        for (GameWrapper gameWrapper : games) {
            Player playerLeft = gameWrapper.game.playerLeft;
            Player playerRight = gameWrapper.game.playerRight;
            PlayerWrapper playerWrapperLeft = getPlayerWrapper(playerLeft);
            PlayerWrapper playerWrapperRight = getPlayerWrapper(playerRight);
            gameWrapper.leftPointsBefore = playerWrapperLeft.currentPoints;
            gameWrapper.rightPointsBefore = playerWrapperRight.currentPoints;

            gameWrapper.leftStake = (int) (gameWrapper.leftPointsBefore * STAKE_PER_GAME);
            gameWrapper.rightStake = (int) (gameWrapper.rightPointsBefore * STAKE_PER_GAME);
            if (gameWrapper.game.pointsLeft > gameWrapper.game.pointsRight) {
                gameWrapper.leftPointsAfter = gameWrapper.leftPointsBefore + gameWrapper.rightStake;
                gameWrapper.rightPointsAfter = gameWrapper.rightPointsBefore - gameWrapper.rightStake;
                playerWrapperLeft.wins++;
                playerWrapperRight.losses++;
            } else if (gameWrapper.game.pointsLeft < gameWrapper.game.pointsRight) {
                gameWrapper.leftPointsAfter = gameWrapper.leftPointsBefore - gameWrapper.leftStake;
                gameWrapper.rightPointsAfter = gameWrapper.rightPointsBefore + gameWrapper.leftStake;
                playerWrapperLeft.losses++;
                playerWrapperRight.wins++;
            } else {
                gameWrapper.leftPointsAfter = gameWrapper.leftPointsBefore;
                gameWrapper.rightPointsAfter = gameWrapper.rightPointsBefore;
            }
            playerWrapperLeft.nbrGames++;
            playerWrapperRight.nbrGames++;
            playerWrapperLeft.currentPoints = gameWrapper.leftPointsAfter;
            playerWrapperRight.currentPoints = gameWrapper.rightPointsAfter;
        }
        for (Iterator<PlayerWrapper> iterator = players.iterator(); iterator.hasNext(); ) {
            PlayerWrapper playerWrapper = iterator.next();
            if (playerWrapper.getNbrGames() == 0) {
                iterator.remove();
            }
        }
    }

    private PlayerWrapper getPlayerWrapper(Player player) {
        for (PlayerWrapper playerWrapper : players) {
            if (playerWrapper.player.id == player.id) {
                return playerWrapper;
            }
        }
        return null;
    }

    public List<GameWrapper> getGames() {
        Collections.sort(games, new Comparator<GameWrapper>() {
            @Override
            public int compare(GameWrapper o1, GameWrapper o2) {
                return o2.game.gameNbr - o1.game.gameNbr;
            }
        });
        return games;
    }

    public List<PlayerWrapper> getPlayers() {
        Collections.sort(players, new Comparator<PlayerWrapper>() {
            @Override
            public int compare(PlayerWrapper o1, PlayerWrapper o2) {
                return o2.currentPoints - o1.currentPoints;
            }
        });
        return players;
    }

    public List<Date> getGameDates() {
        List<Date> gameDates = new ArrayList<Date>();
        for (GameWrapper wrapper : games) {
            Game game = wrapper.game;
            if (!gameDates.contains(game.date)) {
                gameDates.add(game.date);
            }
        }
        Collections.sort(gameDates);
        return gameDates;
    }

    public List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<Player>();
        for (PlayerWrapper wrapper : players) {
            allPlayers.add(wrapper.player);
        }
        return allPlayers;
    }

    public int getPointsAfterGameDate(Date date, Player player) {
        int points = 0;
        for (GameWrapper wrapper : getGames()) {
            if (wrapper.game.date.equals(date) || wrapper.game.date.before(date)) {
                if (wrapper.game.playerLeft.equals(player)) {
                    points = wrapper.getLeftPointsAfter();
                    break;
                } else if (wrapper.game.playerRight.equals(player)) {
                    points = wrapper.getRightPointsAfter();
                    break;
                }
            }
        }
        return points;
    }

    public JsonNode getLineChartJSON() {
        ArrayNode result = JsonUtils.newArrayNode();
        List<PlayerWrapper> playerWrappers = getPlayers();

        ArrayNode columns = JsonUtils.newArrayNode();
        columns.add("Date");
        for (PlayerWrapper playerWrapper : playerWrappers) {
            columns.add(playerWrapper.player.name);
        }
        result.add(columns);

        for (Date date : getGameDates()) {
            ArrayNode row = JsonUtils.newArrayNode();
            row.add(DateFormatUtils.format(date, "yyyy-MM-dd"));
            for (Player player : getAllPlayers()) {
                int pointsAfterGameDate = getPointsAfterGameDate(date, player);
                if (pointsAfterGameDate > 0) {
                    row.add(pointsAfterGameDate);
                } else {
                    row.add((Integer) null);
                }
            }
            result.add(row);
        }

        return result;
    }

    public JsonNode getScatterChartJSON() {
        ArrayNode result = JsonUtils.newArrayNode();
        List<PlayerWrapper> playerWrappers = getPlayers();

        ArrayNode columns = JsonUtils.newArrayNode();
        columns.add("Game");
        for (PlayerWrapper playerWrapper : playerWrappers) {
            columns.add(playerWrapper.player.name);
        }
        result.add(columns);

        for (GameWrapper gameWrapper : games) {
            ArrayNode row = JsonUtils.newArrayNode();
            Game game = gameWrapper.game;
            row.add(game.gameNbr);

            for (PlayerWrapper playerWrapper : playerWrappers) {
                Player player = playerWrapper.player;
                if (player.equals(game.playerLeft)) {
                    row.add(gameWrapper.leftPointsAfter);
                } else if (player.equals(game.playerRight)) {
                    row.add(gameWrapper.rightPointsAfter);
                } else {
                    row.add((Integer) null);
                }
            }
            result.add(row);
        }

        return result;
    }

    public class PlayerWrapper {

        private Player player;

        private int currentPoints;
        private int nbrGames;
        private int wins;
        private int losses;

        public PlayerWrapper(Player player) {
            this.player = player;
            this.currentPoints = STARTING_POINTS;
            this.nbrGames = 0;
            this.wins = 0;
            this.losses = 0;
        }

        public Player getPlayer() {
            return player;
        }

        public int getCurrentPoints() {
            return currentPoints;
        }

        public int getNbrGames() {
            return nbrGames;
        }

        public int getWins() {
            return wins;
        }

        public int getLosses() {
            return losses;
        }
    }

    public class GameWrapper {

        private Game game;

        private int leftPointsBefore;
        private int leftStake;
        private int leftPointsAfter;
        private int rightPointsBefore;
        private int rightStake;
        private int rightPointsAfter;

        public GameWrapper(Game game) {
            this.game = game;
        }

        public Game getGame() {
            return game;
        }

        public int getLeftPointsBefore() {
            return leftPointsBefore;
        }

        public int getLeftStake() {
            return leftStake;
        }

        public int getLeftPointsAfter() {
            return leftPointsAfter;
        }

        public int getRightPointsBefore() {
            return rightPointsBefore;
        }

        public int getRightStake() {
            return rightStake;
        }

        public int getRightPointsAfter() {
            return rightPointsAfter;
        }

        public Player getWinningPlayer() {
            if (game.pointsLeft > game.pointsRight) {
                return game.playerLeft;
            } else if (game.pointsLeft < game.pointsRight) {
                return game.playerRight;
            } else {
                return null;
            }
        }

    }

}
