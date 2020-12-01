package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Movable {
    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;
    private int keys = 0;
    private List<Bomb> bombs = new ArrayList<Bomb>();

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getLives();
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!nextPos.inside(game.getWorld().dimension))
            return false;

        Decor d = game.getWorld().get(nextPos);
        if (d instanceof Box) {
            Position new_pos = direction.nextPosition(nextPos);
            if (game.getWorld().get(new_pos) == null && direction.nextPosition(nextPos).inside(game.getWorld().dimension)) {
                return true;
            }
        }
        return d == null || d instanceof Bonus || d instanceof Key || d instanceof Princess || d instanceof Heart || d instanceof DoorPrevOpened || d instanceof DoorNextOpened;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof Box) {
            game.getWorld().set(direction.nextPosition(nextPos), game.getWorld().get(nextPos));
            game.getWorld().deleteDecor(nextPos);
            game.getWorld().setChange(true);
        }
        setPosition(nextPos);
    }

    public void requestOpen() {
        Position p = direction.nextPosition(getPosition());
        if (game.getWorld().get(p) instanceof DoorNextClosed && keys > 0) {
            game.getWorld().openDoor(p);
            keys--;
        }
    }

    public void update(long now) {
        if (moveRequested && canMove(direction)) {
            doMove(direction);

            if (game.getWorld().get(getPosition()) instanceof BonusBombNbDec) {
                if (getBombs() > 0) {
                    game.getWorld().deleteDecor(getPosition());
                    bombs.remove(bombs.size() - 1);
                    game.getWorld().setChange(true);
                }
            }

            if (game.getWorld().get(getPosition()) instanceof BonusBombNbInc) {
                game.getWorld().deleteDecor(getPosition());
                bombs.add(new Bomb(this.game, getPosition()));
                game.getWorld().setChange(true);
            }

            if (game.getWorld().get(getPosition()) instanceof BonusBombRangeDec) {
                boolean has_decreased = false;
                for (Bomb b : bombs) {
                    if (b.getRange() >= 2) {
                        b.decRange();
                        has_decreased = true;
                    }
                }
                if (has_decreased) {
                    game.getWorld().deleteDecor(getPosition());
                    game.getWorld().setChange(true);
                }
            }

            if (game.getWorld().get(getPosition()) instanceof BonusBombRangeInc) {
                if (!bombs.isEmpty()) {
                    game.getWorld().deleteDecor(getPosition());
                    game.getWorld().setChange(true);
                }
                for (Bomb b : bombs) {
                    b.incRange();
                }
            }

            if (game.getWorld().get(getPosition()) instanceof DoorNextOpened) {
                // TODO
            }

            if (game.getWorld().get(getPosition()) instanceof DoorPrevOpened) {
                // TODO
            }

            if (game.getWorld().get(getPosition()) instanceof Heart) {
                game.getWorld().deleteDecor(getPosition());
                game.getWorld().setChange(true);
                lives++;
            }

            if (game.getWorld().get(getPosition()) instanceof Key) {
                game.getWorld().deleteDecor(getPosition());
                game.getWorld().setChange(true);
                this.keys++;
            }

            if (game.getWorld().get(getPosition()) instanceof Princess) {
                game.getWorld().deleteDecor(getPosition());
                game.getWorld().setChange(true);
                winner = true;
            }

            if (game.getWorld().isThereAMonster(getPosition()))
                lives--;

            if (lives == 0)
                alive = false;
        }
        moveRequested = false;
    }

    public int getBombs() {
        return bombs.size();
    }

    public int getBombsRange() {
        return bombs.get(getBombs() - 1).getRange();
    }

    public int getKeys() {
        return keys;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isWinner() {
        return winner;
    }
}
