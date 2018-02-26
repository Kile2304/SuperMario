package mario.rm.sprite;

import mario.rm.identifier.Direction;
import mario.rm.utility.MoveAttrib;

/**
 *
 * @author mantini.christian
 */
public class Linear implements Azione {

    private int maxX;
    private int maxY;
    private int velX;
    private int velY;
    private int incrX;
    private int incrY;

    public Linear(String script) {
        String[] parameter = script.split(" ");
        try {
            maxX = Integer.parseInt(parameter[2]);
            maxY = Integer.parseInt(parameter[3]);
            velX = Integer.parseInt(parameter[4]);
            velY = Integer.parseInt(parameter[5]);
            incrX = Integer.parseInt(parameter[6]);
            incrY = Integer.parseInt(parameter[7]);
        } catch (ArrayIndexOutOfBoundsException e) {
            if (maxX > 0 && velX == 0) {
                velX = 2;
            }
            if (maxY > 0 && velY == 0) {
                velY = 2;
            }
        }
    }

    /**
     *
     * @param att
     * @param max
     * @param velX
     * @param velY
     * @param movementType
     * @return
     */
    @Override
    public MoveAttrib tick(MoveAttrib att) {
        //System.out.println("asjnaskjdaskdn");

        if (att.getLast().getX() <= att.getFirst().getX() - maxX && att.getDir()[0] == Direction.LEFT
                || att.getLast().getX() >= att.getFirst().getX() + maxX && att.getDir()[0] == Direction.RIGHT) {
            att.setDir((att.getDir()[0] == Direction.LEFT
                    ? Direction.RIGHT
                    : Direction.LEFT), 0);
        }
        att.getLast().increaseXBy(att.getDir()[0] == Direction.LEFT
                ? -velX
                : velX);

        if (att.getLast().getY() <= att.getFirst().getY() - maxY && att.getDir()[1] == Direction.LEFT
                || att.getLast().getY() >= att.getFirst().getY() + maxY && att.getDir()[1] == Direction.RIGHT) {
            att.setDir((att.getDir()[1] == Direction.LEFT
                    ? Direction.RIGHT
                    : Direction.LEFT), 1);
        }
        att.getLast().increaseYBy(att.getDir()[1] == Direction.LEFT
                ? -velY
                : velY);

        return att;
    }

    @Override
    public int getVelX() {
        return velX;
    }

    @Override
    public int getVelY() {
        return velY;
    }
}
