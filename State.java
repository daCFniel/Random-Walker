import java.util.Objects;

/**
 * Wrapper class representing a particular state in a 2D array.
 *
 * @author Daniel Bielech
 */
public class State {

    private int x;
    private int y;

    /**
     * Constructor from coordinates.
     *
     * @param x row
     * @param y column
     */
    public State(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor from state index.
     *
     * @param state    state index
     * @param gridSize numbers of cells in one row
     */
    public State(final String state, final int gridSize) {
        int stateIndex = Integer.parseInt(state);
        int multiplier = (stateIndex - 1) / gridSize;
        int modulo = (stateIndex - 1) % gridSize;
        this.x = multiplier;
        this.y = modulo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        if (x == 0 && y == 0) return 1;
        if (x == 0 && y == 1) return 2;
        if (x == 0 && y == 2) return 3;
        if (x == 1 && y == 0) return 4;
        if (x == 1 && y == 1) return 5;
        if (x == 1 && y == 2) return 6;
        if (x == 2 && y == 0) return 7;
        if (x == 2 && y == 1) return 8;
        if (x == 2 && y == 2) return 9;
        throw new RuntimeException("getIndex: Invalid State");
    }

    @Override
    public String toString() {
        return "State{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return getX() == state.getX() &&
                getY() == state.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}