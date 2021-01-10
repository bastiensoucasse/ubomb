package fr.ubx.poo.game;

import java.util.Random;

public enum Direction {
    N {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x, pos.y - delta);
        }
    },
    E {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x + delta, pos.y);
        }
    },
    S {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x, pos.y + delta);
        }
    },
    W {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x - delta, pos.y);
        }
    },
    ;


    private static final Random randomGenerator = new Random();

    /***
     *
     * @return a pseudorandom direction
     */
    public static Direction random() {
        return values()[randomGenerator.nextInt(values().length)];
    }


    public abstract Position nextPosition(Position pos, int delta);

    final public Position nextPosition(Position pos) {
        return nextPosition(pos, 1);
    }

}