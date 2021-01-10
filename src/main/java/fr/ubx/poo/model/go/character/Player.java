package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
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
    private boolean bombDropped = false;
    private boolean safe = false;

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

    @Override
    public void removeLife() {
        if (safe) {
            System.out.println("Safe for now!");
            return;
        }
        lives--;
        safe = true;

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                safe = false;
            }
        }, 1000);
    }

    public Bomb dropBomb() {
        if (getBombs() < 1)
            return null;

        Bomb bomb = new Bomb(game, getPosition(), getBombsRange());
        removeBomb();
        bombDropped = true;
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

        if (!position.inside(game.getWorld().getDimension()) || game.getWorld().isThereABomb(position)) return false;
        if (game.getWorld().isThereAMovableBox(position, direction)) return true;

        Decor decor = game.getWorld().get(position);
        if (decor == null)
            return true;

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

        Decor decor = game.getWorld().get(getPosition());
        if (decor != null && decor.canGetThrough())
            travel((Door) decor);

        // Check for any monster attack
        if (game.getWorld().isThereAMonster(getPosition()))
            removeLife();
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

    public boolean hasDroppedABomb() {
        return bombDropped;
    }

    public boolean wasLastBomb() {
        return bombs == 0;
    }

    public void setBombDropped(boolean b) {
        bombDropped = b;
    }
}
