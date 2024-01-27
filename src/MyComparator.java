public class MyComparator implements java.util.Comparator<Object> {
    String compKind;
    Player winner;
    public MyComparator(String compKind,Player winner)
    {
        super();
        this.compKind = compKind;
        this.winner = winner;
    }
    @Override
    public int compare(Object o3, Object o4)
    {
        if(compKind.equals("locations") || compKind.equals("kills") || compKind.equals("squares"))
        {
            ConcretePiece o1 = null;
            ConcretePiece o2 = null;
            if (o3 instanceof ConcretePiece && o4 instanceof ConcretePiece)
            {
                o1 = (ConcretePiece) o3;
                o2 = (ConcretePiece) o4;
            }
            if (compKind.equals("locations"))
            {
                if (o1.owner != o2.owner)
                {
                    if ((o1.owner.isPlayerOne() && winner.isPlayerOne()) || (!o1.owner.isPlayerOne() && !winner.isPlayerOne()))
                    {
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                }
                if (o1.getLocations().size() == o2.getLocations().size())
                {
                    return Integer.parseInt(o1.getName().substring(1)) - Integer.parseInt(o2.getName().substring(1));
                }
                return o1.getLocations().size() - o2.getLocations().size();
            }
            if (compKind.equals("kills") && o1 instanceof Pawn && o2 instanceof Pawn)
            {
                if (((Pawn) o1).getKills() == ((Pawn) o2).getKills())
                {
                    if (Integer.parseInt(o1.getName().substring(1)) == Integer.parseInt(o2.getName().substring(1)))
                    {
                        if ((o1.getOwner().isPlayerOne() && winner.isPlayerOne()) || (!o1.getOwner().isPlayerOne() && !winner.isPlayerOne()))
                        {
                            return -1;
                        }
                        else
                        {
                            return 1;
                        }
                    }
                    else
                    {
                        return Integer.parseInt(o1.getName().substring(1)) - Integer.parseInt(o2.getName().substring(1));
                    }
                }
                else
                {
                    return ((Pawn) o2).getKills() - ((Pawn) o1).getKills();
                }
            }
            if (compKind.equals("squares"))
            {
                if (o1.getSquare() == o2.getSquare())
                {
                    if (Integer.parseInt(o1.getName().substring(1)) == Integer.parseInt(o2.getName().substring(1)))
                    {
                        if ((o1.getOwner().isPlayerOne() && winner.isPlayerOne()) || (!o1.getOwner().isPlayerOne() && !winner.isPlayerOne()))
                        {
                            return -1;
                        }
                        else
                        {
                            return 1;
                        }
                    }
                    else
                    {
                        return Integer.parseInt(o1.getName().substring(1)) - Integer.parseInt(o2.getName().substring(1));
                    }
                }
                else
                {
                    return o2.getSquare() - o1.getSquare();
                }
            }
        }
        if(compKind.equals("pieces"))
        {
            String o1 = "";
            String o2 = "";
            if (o3 instanceof String && o4 instanceof String)
            {
                o1 = (String) o3;
                o2 = (String) o4;
            }
            String[] c1 = o1.split(" ");
            String[] c2 = o2.split(" ");
            if(c1[1].charAt(c1.length-1) == c2[1].charAt(c1.length-1))
            {
                if(o1.substring(1,o1.indexOf(",")).equals(o2.substring(1,o2.indexOf(","))))
                {
                    return Integer.parseInt(c1[1].substring(0,c1[1].indexOf(")"))) - Integer.parseInt(c2[1].substring(0,c2[1].indexOf(")")));
                }
                else
                {
                    return (Integer.parseInt(o1.substring(1,o1.indexOf(","))) - Integer.parseInt(o2.substring(1,o2.indexOf(","))));
                }
            }
            else
            {
                return c2[1].charAt(c1.length-1) - (c1[1].charAt(c1.length-1));
            }
        }
        return 0;
    }
}
