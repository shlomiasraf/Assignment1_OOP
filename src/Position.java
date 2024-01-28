public class Position {
    int rowIndex;
    int colIndex;
    public Position(int colIndex,int rowIndex)
    {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
    }

    /**
     *
     * @param b another position
     * @return true if the positions are not the same position, otherwise return false.
     */
    public boolean difPos(Position b)
    {
        if(this.rowIndex != b.rowIndex || this.colIndex != b.colIndex)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @return true if this position is one of the special positions,
     * the positions that if the king on them player1 wins.
     */
    public boolean winningPosition()
    {
        Position position = new Position(this.rowIndex,this.colIndex);
        Position[] winPos = {new Position(0,0),new Position(0,10),new Position(10,0),new Position(10,10)};
        for(int i=0; i < winPos.length; i++)
        {
            if(winPos[i].equals(position))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @brief checks if the objects are equals.
     * @param position the object we want to compare.
     * @return true if the objects are equals.
     */
    @Override
    public boolean equals(Object position)
    {
        Position position1 = (Position) position;
        if (position1.rowIndex == rowIndex && position1.colIndex == colIndex)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *
     * @return this position as a string
     */
    @Override
    public String toString() {
        return "("+colIndex+", "+rowIndex+")";
    }
}
