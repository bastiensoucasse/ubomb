package fr.ubx.poo.model.decor.door;

import fr.ubx.poo.model.decor.Decor;

public class Door extends Decor {
    private final DoorDestination destination;
    private DoorState state;

    public Door(DoorDestination destination, DoorState state) {
        super(state == DoorState.OPENED);
        this.destination = destination;
        this.state = state;
    }

    public DoorDestination getDestination() {
        return destination;
    }

    public DoorState getState() {
        return state;
    }

    public void setState(DoorState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Door";
    }

    @Override
    public Boolean canBePicked() {
        return false;
    }
}
