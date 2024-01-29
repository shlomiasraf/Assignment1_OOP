
public class Pawn extends ConcretePiece{
    private int kills = 0;
    public Pawn(Player owner, String type, String name, String locations)
    {
        super(owner, type, name,locations);
    }

    /**
     *
     * @return the number of pieces this piece killed.
     */
    public int getKills()
    {
        return this.kills;
    }

    /**
     * @brief add 1 kill to the number of kills this piece killed.
     */
    public void addKill()
    {
        this.kills++;
    }

    /**
     * @brief reduce 1 kill to the number of kills this piece killed.
     */
    public void reduceKill()
    {
        this.kills--;
    }
}
