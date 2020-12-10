package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.collectible.Collectible;
import fr.ubx.poo.model.decor.door.Door;
import fr.ubx.poo.model.decor.door.DoorDestination;
import fr.ubx.poo.model.decor.door.DoorState;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.game.Game;

public class Player extends Character {
    private boolean moveRequested = false;
    private boolean openRequested = false;
    private boolean winner;
    private int keys;
    private int bombs;
    private int bombsRange;

    public Player(final Game game, final Position position) {
        super(game, position, Direction.S, game.getLives());
        this.winner = false;
        this.keys = 0;
        this.bombs = 1;
        this.bombsRange = 1;
    }

    public boolean isWinner() {
        return winner;
    }

    public void win() {
        winner = true;
    }

    public int getKeys() {
        return keys;
    }

    public void addKey() {
        keys++;
    }

    public void removeKey() {
        keys--;
    }

    public int getBombs() {
        return bombs;
    }

    public void addBomb() {
        bombs++;
    }

    public void removeBomb() {
        bombs--;
    }

    public int getBombsRange() {
        return bombsRange;
    }

    public void increaseBombsRange() {
        bombsRange++;
    }

    public void decreaseBombsRange() {
        bombsRange--;
    }

    public Bomb dropBomb() {
        if (getBombs() < 1)
            return null;

        Bomb bomb = new Bomb(game, getPosition(), getBombsRange());
        removeBomb();
        game.getWorld().setChanged(true);
        return bomb;
    }

    private void collect(final Collectible collectible) {
        collectible.collect(this);
        game.getWorld().deleteDecor(getPosition());
        game.getWorld().setChanged(true);
    }

    private void travel(final Door door) {
        if (door.getState() != DoorState.OPENED)
            return;

        if (door.getDestination() == DoorDestination.NEXT)
            game.getWorld().levelUp();

        if (door.getDestination() == DoorDestination.PREVIOUS)
            game.getWorld().levelDown();

        game.getWorld().setChanged(true);
    }

    public void requestOpen() {
        openRequested = true;
    }

    public boolean canOpen() {
        Decor decor = game.getWorld().get(getDirection().nextPosition(getPosition()));
        return decor != null && decor.isOpenable(this);
    }

    private void doOpen() {
        ((Door) game.getWorld().get(getDirection().nextPosition(getPosition()))).open(this);
        game.getWorld().setChanged(true);
    }

    public void requestMove(final Direction direction) {
        if (getDirection() != direction)
            setDirection(direction);

        moveRequested = true;
    }

    @Override
    public boolean canMove(final Direction direction) {
        Position position = direction.nextPosition(getPosition());

        if (!position.inside(game.getWorld().getDimension()))
            return false;

        Decor decor = game.getWorld().get(position);

        if (decor == null)
            return true;

        if (isThereAMovableBox()) {
            return true;
        }

        return decor.isWalkable();
    }

    @Override
    public void doMove(final Direction direction) {
        Position position = direction.nextPosition(getPosition());

        //IF basically there's a box
        if (game.getWorld().get(position) != null && game.getWorld().get(position).canBeMoved()) {
            game.getWorld().setDecor(getDirection().nextPosition(position), game.getWorld().get(position));
            game.getWorld().deleteDecor(position);
            game.getWorld().setChanged(true);
        }

        setPosition(position);

        if (game.getWorld().isThereAMonster(getPosition()))
            removeLife();

        Decor decor = game.getWorld().get(getPosition());
        if (decor != null && decor.canGetThrough())
            travel((Door) decor);
    }

    public boolean isThereAMovableBox(){
        Position nextPos = getDirection().nextPosition(getPosition());
        Position next2pos = getDirection().nextPosition(nextPos);
        Position next3pos = getDirection().nextPosition(next2pos);
        //IF there's a box that can be moved
        if (game.getWorld().get(nextPos).canBeMoved() && game.getWorld().get(next2pos) == null) {
            return !game.getWorld().isThereAMonster(next2pos)
                    && next2pos.inside(game.getWorld().getDimension());
        }
        //If there's a box behind another box and it can be moved
        if(game.getWorld().get(nextPos).canBeMoved() && game.getWorld().get(next2pos).canBeMoved()){
            if (game.getWorld().get(next3pos) == null
                    && !game.getWorld().isThereAMonster(next3pos)
                    && next3pos.inside(game.getWorld().getDimension())){
                game.getWorld().setDecor(next3pos, game.getWorld().get(next2pos));
                game.getWorld().deleteDecor(next2pos);
                game.getWorld().setChanged(true);
                return true;
            }
        }
        return false;
    }

    public void update(final long now) {
        Decor decor = game.getWorld().get(getPosition());
        if (decor != null) {
            if (decor.isCollectable())
                collect((Collectible) decor);
        }

        if (openRequested) {
            if (canOpen())
                doOpen();
            openRequested = false;
        }

        if (moveRequested) {
            if (canMove(getDirection()))
                doMove(getDirection());
            moveRequested = false;
        }
    }
}
