import java.util.ArrayList;

public class ConcretePiece implements Piece {
    private Player owner;
    private String type;
    private final String name;
    private ArrayList<String> locations = new ArrayList<>();
    private int square;
    public ConcretePiece(Player owner, String type,String name,String locations)
    {
        this.owner = owner;
        this.type = type;
        this.name = name;
        this.locations.add(locations);
        this.square = 0;
    }

    /**
     *
     * @return the owner of this concrete piece.
     */
    @Override
    public Player getOwner() {
        return this.owner;
    }

    /**
     *
     * @return the type of this concrete piece.
     */
    @Override
    public String getType()
    {
        return this.type;
    }

    /**
     * @brief checks if the objects are equals.
     * @param obj the object we want to compare.
     * @return true if the objects are equals.
     */
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

    /**
     * @brief calculate the cumulative distance this concrete piece has done.
     */
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

    /**
     * @brief removes the last position this concrete piece stepped on.
     */
    public void removeLastLoc()
    {
        this.locations.remove(this.locations.size()-1);
    }

    /**
     *
     * @return the positions this concrete piece stepped on.
     */
    public ArrayList<String> getLocations()
    {
        return this.locations;
    }
    /**
     *
     * @brief add position to the positions this concrete piece stepped on.
     */
    public void addLocation(String loc)
    {
        locations.add(loc);
    }

    /**
     *
     * @return the cumulative distance this concrete piece has done.
     */
    public int getSquare()
    {
        return this.square;
    }

    /**
     *
     * @return the name of this concrete piece.
     */
    public String getName()
    {
        return this.name;
    }
}
