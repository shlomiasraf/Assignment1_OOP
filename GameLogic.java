import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class GameLogic implements PlayableLogic
{
    private final ConcretePlayer player1 = new ConcretePlayer(1);
    private final ConcretePlayer player2 = new ConcretePlayer(2);
    private boolean secPlayerTurn = true;
    private ConcretePiece[][] pieces = new ConcretePiece[11][11];
    private String[][] counterPos = new String[11][11];
    private Stack<Position> last = new Stack<>();
    private Stack<Position> newL = new Stack<>();
    private Stack<ConcretePiece> deadPiece = new Stack<>();
    private Position kingPos = new Position(5,5);
    private boolean dataMember = false;
    private boolean gameFinished = false;

    /**
     * @brief We check if the piece can move and if so we update the variables accordingly.
     * @param a The starting position of the piece.
     * @param b The destination position for the piece.
     * @return true if the piece move from a to b.
     */
    @Override
    public boolean move(Position a, Position b)
    {
        if(!dataMember)
        {
            reset();
        }
        ConcretePiece piece = pieces[a.getColIndex()][a.getRowIndex()];
        if(piece == null)
        {
            return false;
        }
        if(piece.getType().equals("Pawn") && b.winningPosition())
        {
            return false;
        }
        if((piece.getOwner().equals(player1) && isSecondPlayerTurn()) || (piece.getOwner().equals(player2) && !isSecondPlayerTurn()))
        {
            return false;
        }
        if(a.difPos(b) && (a.getRowIndex() == b.getRowIndex() || a.getColIndex() == b.getColIndex()))
        {
            if (a.getRowIndex() == b.getRowIndex())
            {
                for(int i = Math.min(a.getColIndex(),b.getColIndex()); i <= Math.max(a.getColIndex(),b.getColIndex()); i++)
                {
                    Position p1 = new Position(i,a.getRowIndex());
                    if(getPieceAtPosition(p1) != null && !p1.equals(a))
                    {
                        return false;
                    }
                }
            }
            else
            {
                for(int i = Math.min(a.getRowIndex(),b.getRowIndex()); i <= Math.max(a.getRowIndex(),b.getRowIndex()); i++)
                {
                    Position p1 = new Position(a.getColIndex(),i);
                    if(getPieceAtPosition(p1) != null && !p1.equals(a))
                    {
                        return false;
                    }
                }
            }
        }
        else
        {
            return false;
        }
        last.add(a);
        newL.add(b);
        if(counterPos[b.getColIndex()][b.getRowIndex()] == null || !counterPos[b.getColIndex()][b.getRowIndex()].contains(piece.getName()))
        {
            counterPos[b.getColIndex()][b.getRowIndex()] += piece.getName() + ",";
        }
        //move the player.
        pieces[a.getColIndex()][a.getRowIndex()].addLocation(b.toString());
        pieces[b.getColIndex()][b.getRowIndex()] = pieces[a.getColIndex()][a.getRowIndex()];
        pieces[a.getColIndex()][a.getRowIndex()] = null;
        if(piece.getType().equals("King"))
        {
            kingPos = b;
        }
        else
        {
            checkKill();
        }
        if(secPlayerTurn)
        {
            secPlayerTurn = false;
        }
        else
        {
            secPlayerTurn = true;
        }
        if(!gameFinished)
        {
            isGameFinished();
        }
        return true;
    }

    /**
     *
     * @param position The position for which to retrieve the piece.
     * @return the Piece in the Position.
     */
    @Override
    public Piece getPieceAtPosition(Position position)
    {
        return pieces[position.getColIndex()][position.getRowIndex()];
    }

    /**
     *
     * @return the first player.
     */
    @Override
    public Player getFirstPlayer()
    {
        return player1;
    }

    /**
     *
     * @return the second player.
     */
    @Override
    public Player getSecondPlayer()
    {
        return player2;
    }

    /**
     * @brief we return true if player1 in position of win or if player2 in position of win. otherwise we return false.
     * @return true if the game finished.
     */
    @Override
    public boolean isGameFinished() {
        if (!gameFinished)
        {
            ConcretePiece rival1 = null;
            ConcretePiece rival2 = null;
            ConcretePiece rival3 = null;
            ConcretePiece rival4 = null;
            if (kingPos.getColIndex() + 1 <= 10) {
                rival1 = pieces[kingPos.getColIndex() + 1][kingPos.getRowIndex()];
            }
            if (kingPos.getColIndex() - 1 >= 0) {
                rival2 = pieces[kingPos.getColIndex() - 1][kingPos.getRowIndex()];
            }
            if (kingPos.getRowIndex() + 1 <= 10) {
                rival3 = pieces[kingPos.getColIndex()][kingPos.getRowIndex() + 1];
            }
            if (kingPos.getRowIndex() - 1 >= 0) {
                rival4 = pieces[kingPos.getColIndex()][kingPos.getRowIndex() - 1];
            }
            if (kingPos.winningPosition()) {
                //player1 win the game.
                player1.addWins();
                statInfo(player1);
                gameFinished = true;
                return true;
            }
            if (((rival1 != null && rival1.getOwner().equals(getSecondPlayer())) || kingPos.getColIndex() + 1 > 10) && ((rival2 != null && rival2.getOwner().equals(getSecondPlayer())) || kingPos.getColIndex() - 1 < 0) && ((rival3 != null && rival3.getOwner().equals(getSecondPlayer())) || kingPos.getRowIndex() + 1 > 10) && ((rival4 != null && rival4.getOwner().equals(getSecondPlayer())) || kingPos.getRowIndex() - 1 < 0)) {
                //player2 win the game.
                player2.addWins();
                statInfo(player2);
                gameFinished = true;
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return true if it's second player turn.
     */
    @Override
    public boolean isSecondPlayerTurn()
    {
        if(secPlayerTurn)
        {
            return true;
        }
        return false;
    }

    /**
     * @brief reset the board and all variables.
     */
    @Override
    public void reset()
    {
        secPlayerTurn = true;
        pieces = new ConcretePiece[11][11];
        counterPos = new String[11][11];
        createPlayer1Piece();
        createPlayer2Piece();
        last = new Stack<>();
        newL = new Stack<>();
        deadPiece = new Stack<>();
        dataMember = true;
        gameFinished = false;
    }

    /**
     * @brief We implement a return function and return the board to the previous turn using stacks that save for us
     * the last moves, the new moves and the dead pieces.
     */
    @Override
    public void undoLastMove()
    {
        if(!newL.isEmpty())
        {
            while (!deadPiece.isEmpty() && ! newL.isEmpty() && pieces[newL.peek().getColIndex()][newL.peek().getRowIndex()] == null)
            {
                //back from killing
                ConcretePiece piece = deadPiece.pop();
                if(piece instanceof Pawn)
                {
                    //reduce 1 kill from killer piece.
                    ((Pawn) piece).reduceKill();
                }
                //bring a dead piece back to life
                pieces[newL.peek().getColIndex()][newL.pop().getRowIndex()] = deadPiece.pop();
            }
            if (!last.isEmpty()  &&! newL.isEmpty() && ! newL.peek().equals(last.peek()))
            {
                //back to previous position.
                counterPos[newL.peek().getColIndex()][newL.peek().getRowIndex()] = counterPos[newL.peek().getColIndex()][newL.peek().getRowIndex()].substring(0,counterPos[newL.peek().getColIndex()][newL.peek().getRowIndex()].length()-3);
                pieces[newL.peek().getColIndex()][newL.peek().getRowIndex()].removeLastLoc();
                pieces[last.peek().getColIndex()][last.pop().getRowIndex()] = pieces[newL.peek().getColIndex()][newL.peek().getRowIndex()];
                pieces[newL.peek().getColIndex()][newL.pop().getRowIndex()] = null;
                if (secPlayerTurn)
                {
                    secPlayerTurn = false;
                } else {
                    secPlayerTurn = true;
                }
            }
        }
    }

    /**
     *
     * @return the size of the board.
     */
    @Override
    public int getBoardSize()
    {
        reset();
        return 11;
    }

    /**
     *
     * @param winner - the winner of the game.
     * @brief prints all required game statistics by sorting by comparator for each section.
     * */
    public void statInfo(Player winner)
    {
        ArrayList<ConcretePiece> print = new ArrayList<>();
        ArrayList<String> piecesPrint = new ArrayList<>();
        ArrayList<ConcretePiece> printForKill = new ArrayList<>();
        String str = "";
        for(int i=0; i<11; i++)
        {
            for(int j=0; j<11; j++)
            {
                //add pieces to arraylists.
                if (pieces[i][j] != null)
                {
                    pieces[i][j].setSquare();
                    print.add(pieces[i][j]);
                    if(pieces[i][j] instanceof Pawn && ((Pawn) pieces[i][j]).getKills() > 0)
                    {
                        printForKill.add(pieces[i][j]);
                    }
                }
                if (counterPos[i][j] != null && getSize(counterPos[i][j]) > 1)
                {
                    str = "("+i+", "+j+")" +getSize(counterPos[i][j]) + " pieces";
                    piecesPrint.add(str);
                }
            }
        }
        //add the dead pieces to the sort.
        Stack<ConcretePiece> temp = new Stack<>();
        while(!deadPiece.isEmpty())
        {
            deadPiece.pop();
            if(!deadPiece.isEmpty())
            {
                temp.add(deadPiece.pop());
                print.add(temp.peek());
                if(temp.peek() instanceof Pawn && ((Pawn) temp.peek()).getKills() > 0)
                {
                    printForKill.add(temp.peek());
                }
                temp.peek().setSquare();
            }
        }
        //sort and print for section 1.
        Collections.sort(print, new MyComparator("locations",winner));
        for (int i = 0; i < print.size(); i++)
        {
            if(print.get(i).getLocations().size() > 1)
            {
                str = print.get(i).getName() + ": [";
                for (int j = 0; j < print.get(i).getLocations().size(); j++)
                {
                    if (str.charAt(str.length() - 1) == '[')
                    {
                        str += print.get(i).getLocations().get(j);
                    }
                    else
                    {
                        str += ", " + print.get(i).getLocations().get(j);
                    }
                }
                str += "]";
                System.out.println(str);
            }
        }
        System.out.println("***************************************************************************");
        //sort and print for section 2.
        Collections.sort(printForKill, new MyComparator("kills",winner));
        for (int i = 0; i < printForKill.size(); i++)
        {
            System.out.println(printForKill.get(i).getName() + ": " + ((Pawn) printForKill.get(i)).getKills() + " kills");
        }
        //sort and print for section 3.
        System.out.println("***************************************************************************");
        Collections.sort(print, new MyComparator("squares",winner));
        for (int i = 0; i < print.size(); i++)
        {
            if(print.get(i).getSquare() > 0)
            {
                System.out.println(print.get(i).getName() + ": " + print.get(i).getSquare() + " squares");
            }
        }
        //sort and print for section 4.
        System.out.println("***************************************************************************");
        Collections.sort(piecesPrint, new MyComparator("pieces",winner));
        for (int i = 0; i < piecesPrint.size(); i++)
        {
            System.out.println(piecesPrint.get(i));
        }
        System.out.println("***************************************************************************");
    }

    /**
     * @brief create all the player1 pieces on the board.
     */
    public void createPlayer1Piece()
    {
        Pawn p1 = new Pawn(player1, "Pawn", "D1","(5, 3)");
        pieces[5][3] = p1;
        counterPos[5][3] += "D1,";
        p1 = new Pawn(player1, "Pawn","D13","(5, 7)");
        pieces[5][7] = p1;
        counterPos[5][7]+= ("D13,");
        int k =2;
        int j = 10;
        for(int i = 0; i <= 2; i++)
        {
            p1 = new Pawn(player1, "Pawn","D"+k,"("+(4+i)+", 4)");
            pieces[4+i][4] = p1;
            counterPos[4+i][4]+=("D"+k+",");
            p1 = new Pawn(player1, "Pawn", "D"+j,"("+(4+i)+", 6)");
            pieces[4+i][6] = p1;
            counterPos[4+i][6]+=("D"+j+",");
            k++;
            j++;
        }
        k =5;
        for (int i = 0; i <=4; i++)
        {
            if(i != 2)
            {
                p1 = new Pawn(player1, "Pawn","D"+k,"("+(3+i)+", 5)");
                pieces[3+i][5] = p1;
                counterPos[3+i][5] +=("D"+k+",");
            }
            else
            {
                King p2 = new King(player1, "King", "K7","("+(3+i)+", 5)");
                pieces[3+i][5] = p2;
                counterPos[3+i][5] += "K7,";
                kingPos = new Position(3+i,5);
            }
            k++;
        }
    }

    /**
     * @brief create all the player2 pieces on the board.
     */
    public void createPlayer2Piece()
    {
        Pawn p2 = new Pawn(player2, "Pawn", "A12","(1, 5)");
        pieces[1][5] = p2;
        counterPos[1][5] += "A12,";
        p2 = new Pawn(player2, "Pawn", "A6","(5, 1)");
        pieces[5][1] = p2;
        counterPos[5][1] += "A6,";
        p2 = new Pawn(player2, "Pawn", "A19","(5, 9)");
        pieces[5][9] = p2;
        counterPos[5][9] += "A19,";
        p2 = new Pawn(player2, "Pawn", "A13","(9, 5)");
        pieces[9][5] = p2;
        counterPos[9][5] += "A13,";
        int k =7;
        int j = 1;
        int f = 8;
        for(int i = 3; i<=7; i++)
        {
            if(k == 13)
            {
                k =15;
            }
            if(f == 12)
            {
                f =14;
            }
            p2 = new Pawn(player2, "Pawn","A"+k,"(0, " + i+")");
            pieces[0][i] = p2;
            counterPos[0][i] += "A"+k+",";
            p2 = new Pawn(player2, "Pawn","A"+j,"("+i+", 0)");
            pieces[i][0] = p2;
            counterPos[i][0] += "A"+j+",";
            p2 = new Pawn(player2, "Pawn","A"+(j+19),"("+i+", 10)");
            pieces[i][10] = p2;
            counterPos[i][10] += "A"+(j+19)+",";
            p2 = new Pawn(player2, "Pawn","A"+f,"(10, "+i+")");
            pieces[10][i] = p2;
            counterPos[10][i] += ("A"+f+ ",");
            k+=2;
            f+=2;
            j++;
        }
    }

    /**
     * @brief checks if a kill should be performed due to the move and if so, performs it.
     */
    public void checkKill()
    {
        if(!newL.isEmpty())
        {
            ConcretePiece killerPiece = pieces[newL.peek().getColIndex()][newL.peek().getRowIndex()];
            ArrayList<Position> enemyPos = killEnemy(killerPiece,newL.peek());
            for (int i=0; i< enemyPos.size(); i++)
            {
                if(killerPiece instanceof Pawn)
                {
                    ((Pawn) killerPiece).addKill();
                }
                //perform the kill.
                newL.add(new Position(enemyPos.get(i).getColIndex(),enemyPos.get(i).getRowIndex()));
                deadPiece.add(pieces[enemyPos.get(i).getColIndex()][enemyPos.get(i).getRowIndex()]);
                deadPiece.add(killerPiece);
                pieces[enemyPos.get(i).getColIndex()][enemyPos.get(i).getRowIndex()] = null;
            }
        }
    }

    /**
     *
     * @param piece - the killerPiece, the piece that going to kill other piece.
     * @param position - the position of this killerPiece.
     * @return arraylist of all the positions of pieces that die from this move.
     */
    public ArrayList<Position> killEnemy(ConcretePiece piece,Position position)
    {
        ArrayList<Position> enemyPos = new ArrayList<>();
        ConcretePiece rival1 = null;
        ConcretePiece rival2 = null;
        ConcretePiece rival3 = null;
        ConcretePiece rival4 = null;
        if (position.getColIndex()+1 <= 10) {
            rival1 = pieces[position.getColIndex()+1][position.getRowIndex()];
        }
        if(position.getColIndex()-1 >= 0) {
            rival2 = pieces[position.getColIndex() - 1][position.getRowIndex()];
        }
        if(position.getRowIndex()+1 <= 10) {
            rival3 = pieces[position.getColIndex()][position.getRowIndex() + 1];
        }
        if(position.getRowIndex()-1 >= 0) {
            rival4 = pieces[position.getColIndex()][position.getRowIndex() - 1];
        }
        if(rival1 != null && !rival1.getOwner().equals(piece.getOwner()) && !rival1.getType().equals("King"))
        {
            Position pos = new Position(position.getColIndex()+2,position.getRowIndex());
            if((position.getColIndex() +2) <=10  && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.getColIndex() + 2][position.getRowIndex()];
                if (partner != null && !rival1.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.getColIndex() + 1, position.getRowIndex()));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.getColIndex() + 1, position.getRowIndex()));
            }
        }
        if(rival2 != null && !rival2.getOwner().equals(piece.getOwner()) && !rival2.getType().equals("King"))
        {
            Position pos = new Position(position.getColIndex()-2,position.getRowIndex());
            if((position.getColIndex()-2) >= 0 && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.getColIndex() - 2][position.getRowIndex()];
                if (partner != null && !rival2.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.getColIndex() - 1, position.getRowIndex()));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.getColIndex() - 1, position.getRowIndex()));
            }
        }
        if(rival3 != null && !rival3.getOwner().equals(piece.getOwner()) && !rival3.getType().equals("King"))
        {
            Position pos = new Position(position.getColIndex(),position.getRowIndex()+2);
            if((position.getRowIndex()+2) <= 10 && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.getColIndex()][position.getRowIndex() + 2];
                if (partner != null && !rival3.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.getColIndex(), position.getRowIndex() + 1));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.getColIndex(), position.getRowIndex() + 1));
            }
        }
        if(rival4 != null && !rival4.getOwner().equals(piece.getOwner()) && !rival4.getType().equals("King"))
        {
            Position pos = new Position(position.getColIndex(), position.getRowIndex()-2);
            if((position.getRowIndex()-2) >= 0 && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.getColIndex()][position.getRowIndex()-2];
                if (partner != null && !rival4.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.getColIndex(),position.getRowIndex()-1));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.getColIndex(),position.getRowIndex()-1));
            }
        }
        return enemyPos;
    }

    /**
     *
     * @param str the string we get.
     * @return check the length of this string by "," split.
     */
    public int getSize(String str)
    {
        String[] s1 = str.split(",");
        return s1.length;
    }
}
