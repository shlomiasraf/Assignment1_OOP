import java.util.ArrayList;

public class Pawn extends ConcretePiece{
    int kills = 0;
    public Pawn(Player owner, String type, String name, String locations)
    {
        super(owner, type, name,locations);
    }
    public int getKills()
    {
        return this.kills;
    }
    public void addKill()
    {
        this.kills++;
    }
    public void reduceKill()
    {
        this.kills++;
    }
}
