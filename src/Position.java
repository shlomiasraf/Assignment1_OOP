public class Position {
    int rowIndex;
    int colIndex;
    public Position(int colIndex,int rowIndex)
    {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
    }
    public boolean difPos(Position b)
    {
        if(this.rowIndex != b.rowIndex || this.colIndex != b.colIndex)
        {
            return true;
        }
        return false;
    }
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

    @Override
    public String toString() {
        return "("+colIndex+", "+rowIndex+")";
    }
}
