/**
 * A position.
 */
public class Position {
    private static final int HASH_PRIME = 31, DEFAULT_X = 0, DEFAULT_Y = 0;
    protected int x, y;

    /**
     * Creates a position at DEFAULT_X, DEFAULT_Y.
     */
    public Position() {
        this(DEFAULT_X, DEFAULT_Y);
    }

    /**
     * Creates a deep clone of the given position.
     * @param position the position
     */
    public Position(Position position) {
        this(position.x, position.y);
    }

    /**
     * Creates a position.
     * @param x the x value
     * @param y the y value
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the distance between this position and another position.
     * @param position the other position
     * @return the distance between this position and the other position
     */
    public int getDistance(Position position) {
            int distanceX = x - position.x;
            int distanceY = y - position.y;

            return (int) Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));
    }

    /**
     * Gets the hash code.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return HASH_PRIME * x * y;
    }

    /**
     * Checks if this object is equal to another object.
     * @param object the other object
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object object) {
        // reflexive test
        if(this == object) {
            return true;
        }

        // null test
        if(object == null) {
            return false;
        }

        // symmetry test
        if(getClass() != object.getClass()) {
            return false;
        }

        Position position = (Position) object;

        return position.x == x && position.y == y;
    }

    /**
     * Gets a string representation of this object.
     * @return the string representation of this object
     */
    @Override
    public String toString() {
        return "Position [x=" + x + ", y=" + y + "]";
    }
}
