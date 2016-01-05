import java.util.Random;

/**
 * A SnakeAI.
 */
public class SnakeAI {
    private final int CHANCE_MAX = 100, DO_NOT_TURN_CHANCE = 5, GO_FOR_CLOSEST_FOOD_CHANCE = 75, GO_MOST_SNAKE_FREE_CHANCE = 60, GO_STRAIGHT_CHANCE = 95;
    private Snake snake;
    private final Random random;
    private boolean previousMoveWasTurn;
    private int turnsInARow;
    private Food goalFood;

    /**
     * Creates a SnakeAI.
     * @param snake the snake that will use this AI
     */
    public SnakeAI(Snake snake) {
        this.snake = snake;

        random = new Random();
        previousMoveWasTurn = false;
        turnsInARow = 0;
        goalFood = null;
    }

    /**
     * Gets the goal food.
     * @return the goal food
     */
    public Food getGoalFood() {
        return goalFood;
    }

    /**
     * Removes the goal food.
     */
    public void removeGoalFood() {
        goalFood = null;
    }

    /**
     * Gets the AI's choice for a direction to go in.
     * @return the AI's choice for the direction to go in
     */
    public int getAIDirection() {
        int aiDirection = getBestAIDirection();
        Position movedPosition = snake.getMovedPosition(aiDirection, snake.position);
        boolean willEatFood = snake.willEat(movedPosition);
        int multiplier = willEatFood ? 2 : 1;

        // attempt to not turn
        int doNotTurnChance = random.nextInt(CHANCE_MAX);
        if(doNotTurnChance < turnsInARow * multiplier * DO_NOT_TURN_CHANCE && !snake.spotWillCollide(snake.direction)) {
            aiDirection = snake.direction;
        }

        // set chance of not turning
        previousMoveWasTurn= aiDirection != snake.direction;
        if(previousMoveWasTurn) {
            turnsInARow++;
        }
        else {
            if(turnsInARow > 0) {
                turnsInARow--;
            }
        }

        return aiDirection;
    }

    /**
     * Gets the AI's best choice for a direction to go in.
     * @return the AI's best choice for the direction to go in
     */
    private int getBestAIDirection() {
        // attempt to go in closest food direction
        int goForClosestFoodChance = random.nextInt(CHANCE_MAX);
        if(goForClosestFoodChance < GO_FOR_CLOSEST_FOOD_CHANCE) {
            int closetFoodDirection = getClosestFoodDirection();

            if(snake.isLegalDirectionChange(closetFoodDirection) && !snake.spotWillCollide(closetFoodDirection)) {
                return closetFoodDirection;
            }
        }

        // attempt to get the direction most free of snakes
        int goMostSnakeFreeChance = random.nextInt(CHANCE_MAX);
        if(goMostSnakeFreeChance < GO_MOST_SNAKE_FREE_CHANCE) {
            int mostSnakeFreeDirection = getMostSnakeFreeDirection();

            if(snake.isLegalDirectionChange(mostSnakeFreeDirection) && !snake.spotWillCollide(mostSnakeFreeDirection)) {
                return mostSnakeFreeDirection;
            }
        }

        // attempt to go in straight direction
        int goStraightChance = random.nextInt(CHANCE_MAX);
        if(goStraightChance < GO_STRAIGHT_CHANCE) {
            if(!snake.spotWillCollide(snake.direction)) {
                return snake.direction;
            }
        }

        // attempt to go in a random direction
        int randomDirection = getRandomDirection();
        if(snake.isLegalDirectionChange(randomDirection) && !snake.spotWillCollide(randomDirection)) {
            return randomDirection;
        }

        return snake.direction;
    }

    /**
     * Gets the closest food direction.
     * @return the closest food direction
     */
    private int getClosestFoodDirection() {
        Food closestFood = null;
        if(goalFood == null) {
            int smallestDistance = Integer.MAX_VALUE;
            for(Food food : snake.getFoodList()) {
                int distance = snake.position.getDistance(food.position);
                if(distance < smallestDistance) {
                    boolean foodIsInTail = false;
                    for(MoveableBlock tail : snake.getTailList()) {
                        if(tail.position.equals(food.position)) {
                            foodIsInTail = true;

                            break;
                        }
                    }

                    // make sure the food is not inside myself
                    if(!foodIsInTail) {
                        boolean isOtherSnakesGoal = false;
                        // check out the other snakes' goal food and see if we are closer
                        for(Snake otherSnake : snake.getSnakeList()) {
                            Food snakeGoalFood = otherSnake.getGoalFood();

                            if(snakeGoalFood != null && snakeGoalFood.position.equals(food.position)) {
                                if(distance > otherSnake.position.getDistance(food.position)) {
                                    isOtherSnakesGoal = true;

                                    break;
                                }
                                else {
                                    // tell the other guy I took it
                                    snake.notifySnakeList(food);
                                }
                            }
                        }

                        // if the other snakes don't have it as a goal we can set it as our goal
                        if(!isOtherSnakesGoal) {
                            smallestDistance = distance;

                            closestFood = food;
                        }
                    }
                }
            }

            goalFood = closestFood;
        }
        else {
            closestFood = goalFood;
        }

        // if we found a closest food
        int closetFoodDirection = -1;
        if(closestFood != null) {
            if(snake.position.y > closestFood.position.y && snake.isLegalDirectionChange(Snake.UP) && !snake.spotWillCollide(Snake.UP)) {
                closetFoodDirection = Snake.UP;
            }
            else if(snake.position.y < closestFood.position.y && snake.isLegalDirectionChange(Snake.DOWN) && !snake.spotWillCollide(Snake.DOWN)) {
                closetFoodDirection = Snake.DOWN;
            }
            else if(snake.position.x > closestFood.position.x && snake.isLegalDirectionChange(Snake.LEFT) && !snake.spotWillCollide(Snake.LEFT)) {
                closetFoodDirection = Snake.LEFT;
            }
            else if(snake.position.x < closestFood.position.x && snake.isLegalDirectionChange(Snake.RIGHT) && !snake.spotWillCollide(Snake.RIGHT)) {
                closetFoodDirection = Snake.RIGHT;
            }
        }

        return closetFoodDirection;
    }

    /**
     * Gets a random direction.
     * @return the random direction
     */
    private int getRandomDirection() {
        int tries = 8;
        while(tries > 0) {
            int randomDirection = random.nextInt(Snake.RIGHT + 1);

            if(snake.isLegalDirectionChange(randomDirection) && !snake.spotWillCollide(randomDirection)) {
                return randomDirection;
            }

            tries--;
        }

        return -1;
    }

    /**
     * Gest the most snake free direction.
     * @return the most snake free direction
     */
    private int getMostSnakeFreeDirection() {
        int goUp = 0;
        int goDown = 0;
        int goLeft = 0;
        int goRight = 0;

        for(Snake otherSnake : snake.getSnakeList()) {
            if(!otherSnake.isDead()) {
                int freeSpotsAbove = otherSnake.maxPosition.y;
                int freeSpotsBelow = otherSnake.maxPosition.y;
                int freeSpotsOnLeft = otherSnake.maxPosition.x;
                int freeSpotsOnRight = otherSnake.maxPosition.x;

                for(MoveableBlock tail : snake.getTailList()) {
                    // same column
                    if(snake.position.x == tail.position.x) {
                        if(snake.position.y < tail.position.y) {
                            freeSpotsAbove--;
                        }
                        else if(snake.position.y > tail.position.y) {
                            freeSpotsBelow--;
                        }
                    }

                    // same row
                    if(snake.position.y == tail.position.y) {
                        if(snake.position.x > tail.position.x) {
                            freeSpotsOnLeft--;
                        }
                        else if(snake.position.x < tail.position.x) {
                            freeSpotsOnRight--;
                        }
                    }
                }

                if(isLargestOf(freeSpotsAbove, freeSpotsBelow, freeSpotsOnLeft, freeSpotsOnRight) &&
                        snake.isLegalDirectionChange(Snake.UP) && !snake.spotWillCollide(Snake.UP)) {
                    goUp++;
                }
                else if(isLargestOf(freeSpotsBelow, freeSpotsAbove, freeSpotsOnLeft, freeSpotsOnRight) &&
                        snake.isLegalDirectionChange(Snake.DOWN) && !snake.spotWillCollide(Snake.DOWN)) {
                    goDown++;
                }
                else if(isLargestOf(freeSpotsOnLeft, freeSpotsAbove, freeSpotsBelow, freeSpotsOnRight) &&
                        snake.isLegalDirectionChange(Snake.LEFT) && !snake.spotWillCollide(Snake.LEFT)) {
                    goLeft++;
                }
                else if(isLargestOf(freeSpotsOnRight, freeSpotsAbove, freeSpotsBelow, freeSpotsOnLeft) &&
                        snake.isLegalDirectionChange(Snake.RIGHT) && !snake.spotWillCollide(Snake.RIGHT)) {
                    goRight++;
                }
            }
        }

        int mostSnakeFreeDirection = -1;
        if(isLargestOf(goUp, goDown, goLeft, goRight) &&
                snake.isLegalDirectionChange(Snake.UP) && !snake.spotWillCollide(Snake.UP)) {
            mostSnakeFreeDirection = Snake.UP;
        }
        else if(isLargestOf(goDown, goUp, goLeft, goRight) &&
                snake.isLegalDirectionChange(Snake.DOWN) && !snake.spotWillCollide(Snake.DOWN)) {
            mostSnakeFreeDirection = Snake.DOWN;
        }
        else if(isLargestOf(goLeft, goUp, goDown, goRight) &&
                snake.isLegalDirectionChange(Snake.LEFT) && !snake.spotWillCollide(Snake.LEFT)) {
            mostSnakeFreeDirection = Snake.LEFT;
        }
        else if(isLargestOf(goRight, goUp, goDown, goLeft) &&
                snake.isLegalDirectionChange(Snake.RIGHT) && !snake.spotWillCollide(Snake.RIGHT)) {
            mostSnakeFreeDirection = Snake.RIGHT;
        }

        return mostSnakeFreeDirection;
    }

    /**
     * Tests if a number is the largest of a set.
     * @param toTest the number to test
     * @param a a number to test against
     * @param b a number to test against
     * @param c a number to test against
     * @return true is the number is the largest of the set
     */
    private boolean isLargestOf(int toTest, int a, int b, int c) {
        return toTest >= a && toTest >= b && toTest >= c;
    }
}
