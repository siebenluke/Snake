/**
 * A teleporter.
 */
public class Teleporter {
    protected Block entranceBlock, exitBlock;

    /**
     * Creates a teleporter.
     * @param minPosition the teleporter's min position
     * @param maxPosition the teleporter's max position
     */
    public Teleporter(Position minPosition, Position maxPosition) {
        entranceBlock = new Block(minPosition, maxPosition);
        exitBlock = new Block(minPosition, maxPosition);

        teleport();
    }

    /**
     * Teleports the teleporters.
     */
    public void teleport() {
        entranceBlock.teleport();
        teleport(entranceBlock, exitBlock);
    }

    /**
     * Teleports the teleporter's blocks.
     */
    private void teleport(Block block, Block blockToTeleport) {
        // get a different position for the teleporter's exit
        do {
            blockToTeleport.teleport();
        } while(blockToTeleport.position.equals(block.position));
    }

    /**
     * Gets the other teleporter block.
     * @param block the block to check
     * @return the other teleporter block, or the passed in block if its not one of the teleporter's blocks
     */
    public boolean teleportOtherBlock(Block block) {
        if(block.equals(entranceBlock)) {
            teleport(entranceBlock, exitBlock);

            return true;
        }
        else if(block.equals(exitBlock)) {
            teleport(exitBlock, entranceBlock);

            return true;
        }

        return false;
    }
}
