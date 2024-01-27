import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class ConcretePiece implements Piece {
    Player owner;
    String type;
    String name;
    ArrayList<String> locations = new ArrayList<>();
    int square = 0;
    public ConcretePiece(Player owner, String type,String name,String locations)
    {
        this.owner = owner;
        this.type = type;
        this.name = name;
        this.locations.add(locations);
    }
    @Override
    public Player getOwner() {
        return this.owner;
    }
    @Override
    public String getType()
    {
        return this.type;
    }
    @Override
    public boolean equals(Object obj)
    {
        ConcretePiece piece = (ConcretePiece) obj;
        if(piece.getOwner() == owner && piece.getType().equals(type))
        {
            return true;
        }
        return false;
    }
    public void setSquare()
    {
        if(getLocations().size() > 1)
        {
            for (int i = 1; i < getLocations().size(); i++)
            {
                int x1 = Integer.parseInt(getLocations().get(i).substring(1,(getLocations().get(i).indexOf(","))));
                int x2 = Integer.parseInt(getLocations().get(i-1).substring(1,(getLocations().get(i-1).indexOf(","))));
                int y1 = Integer.parseInt(getLocations().get(i).substring((getLocations().get(i).indexOf(",")+2),getLocations().get(i).length()-1));
                int y2 = Integer.parseInt(getLocations().get(i-1).substring((getLocations().get(i-1).indexOf(",")+2),getLocations().get(i-1).length()-1));
                this.square += Math.hypot(x1-x2, y1-y2);
            }
        }
    }
    public void removeLastLoc()
    {
        this.locations.remove(this.locations.size()-1);
    }
    public ArrayList<String> getLocations()
    {
        return this.locations;
    }
    public int getSquare()
    {
        return this.square;
    }
    public String getName()
    {
        return this.name;
    }
}
