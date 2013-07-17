package util;

import models.Game;
import models.Player;

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
        for (int i = 0; i < games.size(); i++) {
            GameWrapper wrapper = games.get(i);

            Player playerLeft = wrapper.game.playerLeft;
            Player playerRight = wrapper.game.playerRight;
            PlayerWrapper playerWrapperLeft = getPlayerWrapper(playerLeft);
            PlayerWrapper playerWrapperRight = getPlayerWrapper(playerRight);
            wrapper.leftPointsBefore = playerWrapperLeft.currentPoints;
            wrapper.rightPointsBefore = playerWrapperRight.currentPoints;

            wrapper.leftStake = (int) (wrapper.leftPointsBefore * STAKE_PER_GAME);
            wrapper.rightStake = (int) (wrapper.leftPointsBefore * STAKE_PER_GAME);
            if (wrapper.game.pointsLeft > wrapper.game.pointsRight) {
                wrapper.leftPointsAfter = wrapper.leftPointsBefore + wrapper.leftStake;
                wrapper.rightPointsAfter = wrapper.rightPointsBefore - wrapper.rightStake;
                playerWrapperLeft.wins++;
                playerWrapperRight.losses++;
            } else if (wrapper.game.pointsLeft < wrapper.game.pointsRight) {
                wrapper.leftPointsAfter = wrapper.leftPointsBefore - wrapper.leftStake;
                wrapper.rightPointsAfter = wrapper.rightPointsBefore + wrapper.rightStake;
                playerWrapperLeft.losses++;
                playerWrapperRight.wins++;
            } else {
                wrapper.leftPointsAfter = wrapper.leftPointsBefore;
                wrapper.rightPointsAfter = wrapper.rightPointsBefore;
            }
            playerWrapperLeft.nbrGames++;
            playerWrapperRight.nbrGames++;
            playerWrapperLeft.currentPoints = wrapper.leftPointsAfter;
            playerWrapperRight.currentPoints = wrapper.rightPointsAfter;
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
                return Integer.compare(o1.game.gameNbr, o2.game.gameNbr);
            }
        });
        return games;
    }

    public List<PlayerWrapper> getPlayers() {
        Collections.sort(players, new Comparator<PlayerWrapper>() {
            @Override
            public int compare(PlayerWrapper o1, PlayerWrapper o2) {
                return -Integer.compare(o1.currentPoints, o2.currentPoints);
            }
        });
        return players;
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
