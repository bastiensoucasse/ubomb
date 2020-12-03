package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.Bonus.*;
import fr.ubx.poo.model.decor.door.*;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Movable {
    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private boolean openRequested = false;
    private int lives = 1;
    private boolean winner;
    private int keys = 0;
    private List<Bomb> bombs = new ArrayList<Bomb>();

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getLives();
        bombs.add(new Bomb(game, getPosition()));
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void update(long now) {
        // Move requested handler
        if (moveRequested && canMove(direction)) {
            doMove(direction);

            if (game.getWorld().get(getPosition()) instanceof Bonus) {
                Bonus b = (Bonus) game.getWorld().get(getPosition());
                b.pick(game.getPlayer());
                game.getWorld().setChanged(true);
            }

            if (game.getWorld().get(getPosition()) instanceof Door)
                travel((Door) game.getWorld().get(getPosition()));

            if (game.getWorld().get(getPosition()) instanceof Heart) {
                game.getWorld().deleteDecor(getPosition());
                game.getWorld().setChanged(true);
                lives++;
            }

            if (game.getWorld().get(getPosition()) instanceof Key) {
                game.getWorld().deleteDecor(getPosition());
                game.getWorld().setChanged(true);
                this.keys++;
            }

            if (game.getWorld().get(getPosition()) instanceof Princess) {
                game.getWorld().deleteDecor(getPosition());
                game.getWorld().setChanged(true);
                winner = true;
            }

            if (game.getWorld().isThereAMonster(getPosition()))
                lives--;

            if (lives == 0)
                alive = false;

            moveRequested = false;
        }

        // Open requested handler
        if (openRequested && canOpen()) {
            doOpen();
            openRequested = false;
        }
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction)
            this.direction = direction;
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!nextPos.inside(game.getWorld().getDimension()))
            return false;

        Decor decor = game.getWorld().get(nextPos);
        if (decor == null)
            return true;

        if (decor instanceof Box) {
            Position new_pos = direction.nextPosition(nextPos);
            //Si la new pos est vide et qu'il n'y a pas de monstre et qu'elle est bien dans la dimension du jeu
            if (game.getWorld().get(new_pos) == null && !game.getWorld().isThereAMonster(new_pos) && direction.nextPosition(nextPos).inside(game.getWorld().getDimension())) {
                return true;
            }
            //Cas pour les doubles caisses.
            if (game.getWorld().get(new_pos) instanceof Box && game.getWorld().get(direction.nextPosition(new_pos)) == null
                    && direction.nextPosition(new_pos).inside(game.getWorld().getDimension()) && !game.getWorld().isThereAMonster(direction.nextPosition(new_pos))) {
                game.getWorld().set(direction.nextPosition(new_pos), game.getWorld().get(new_pos));
                game.getWorld().deleteDecor(new_pos);
                game.getWorld().setChanged(true);
            }
        }

        return decor.isWalkable();
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        if (game.getWorld().get(nextPos) instanceof Box) {
            game.getWorld().set(direction.nextPosition(nextPos), game.getWorld().get(nextPos));
            game.getWorld().deleteDecor(nextPos);
            game.getWorld().setChanged(true);
        }
        setPosition(nextPos);
    }

    public void requestOpen() {
        openRequested = true;
    }

    private boolean canOpen() {
        Decor decor = game.getWorld().get(direction.nextPosition(getPosition()));
        if (!(decor instanceof Door)) return false;
        return ((Door)decor).getState() == DoorState.CLOSED && keys > 0;
    }

    private void doOpen() {
        ((Door) game.getWorld().get(direction.nextPosition(getPosition()))).setState(DoorState.OPENED);
    }

    private void travel(Door door) {
        if (door.getState() != DoorState.OPENED)
            return;

        if (door.getDestination() == DoorDestination.NEXT)
            game.getWorld().levelUp();

        if (door.getDestination() == DoorDestination.PREVIOUS)
            game.getWorld().levelDown();
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

    public Bomb dropABomb() {
        Bomb b = null;
        if(bombs.size() > 0){
            b = bombs.get(getBombs()-1);
            b.setPosition(getPosition());
            game.getWorld().setChanged(true);
            bombs.remove(b);
        }
        return b;
    }

    public void decBombs() {
        bombs.remove(getBombs()-1);
    }

    public void addBomb(){
        bombs.add(new Bomb(game, getPosition()));
    }

    public boolean decRange() {
        boolean has_decreased=false;
        for(Bomb b : bombs){
            if(b.getRange()>1) {
                b.decRange();
                has_decreased = true;
            }
        }
        return has_decreased;
    }

    public void incRange() {
        for(Bomb b : bombs){
            b.incRange();
        }
    }
}
