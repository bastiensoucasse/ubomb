package fr.ubx.poo.model.decor.door;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public class Door extends Decor {
    private final DoorDestination destination;
    private DoorState state;

    public Door(final DoorDestination destination, final DoorState state) {
        super(false, state == DoorState.OPENED);
        this.destination = destination;
        this.state = state;
    }

    public DoorDestination getDestination() {
        return destination;
    }

    public DoorState getState() {
        return state;
    }

    public void setState(final DoorState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Door";
    }

    @Override
    public boolean canGetThrough() {
        return state == DoorState.OPENED;
    }

    @Override
    public boolean isOpenable(final Player player) {
        return getState() == DoorState.CLOSED && player.getKeys() > 0;
    }

    public void open(final Player player) {
        setState(DoorState.OPENED);
        setWalkable(true);
        player.removeKey();
    }
}
