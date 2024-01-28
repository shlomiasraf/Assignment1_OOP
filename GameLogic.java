import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class GameLogic implements PlayableLogic
{
    ConcretePlayer player1 = new ConcretePlayer(1,0);
    ConcretePlayer player2 = new ConcretePlayer(2,0);
    boolean secPlayerTurn = true;
    ConcretePiece[][] pieces = new ConcretePiece[11][11];
    String[][] counterPos = new String[11][11];
    Stack<Position> last = new Stack<>();
    Stack<Position> newL = new Stack<>();
    Stack<ConcretePiece> deadPiece = new Stack<>();
    Position kingPos = new Position(5,5);
    boolean dataMember = false;
    boolean gameFinished = false;

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
        ConcretePiece piece = pieces[a.colIndex][a.rowIndex];
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
        if(a.difPos(b) && (a.rowIndex == b.rowIndex || a.colIndex == b.colIndex))
        {
            if (a.rowIndex == b.rowIndex)
            {
                for(int i = Math.min(a.colIndex,b.colIndex); i <= Math.max(a.colIndex,b.colIndex); i++)
                {
                    Position p1 = new Position(i,a.rowIndex);
                    if(getPieceAtPosition(p1) != null && !p1.equals(a))
                    {
                        return false;
                    }
                }
            }
            else
            {
                for(int i = Math.min(a.rowIndex,b.rowIndex); i <= Math.max(a.rowIndex,b.rowIndex); i++)
                {
                    Position p1 = new Position(a.colIndex,i);
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
        if(counterPos[b.colIndex][b.rowIndex] == null || !counterPos[b.colIndex][b.rowIndex].contains(piece.name))
        {
            counterPos[b.colIndex][b.rowIndex] += piece.name + ",";
        }
        //move the player.
        pieces[a.colIndex][a.rowIndex].locations.add(b.toString());
        pieces[b.colIndex][b.rowIndex] = pieces[a.colIndex][a.rowIndex];
        pieces[a.colIndex][a.rowIndex] = null;
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
        return pieces[position.colIndex][position.rowIndex];
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
            if (kingPos.colIndex + 1 <= 10) {
                rival1 = pieces[kingPos.colIndex + 1][kingPos.rowIndex];
            }
            if (kingPos.colIndex - 1 >= 0) {
                rival2 = pieces[kingPos.colIndex - 1][kingPos.rowIndex];
            }
            if (kingPos.rowIndex + 1 <= 10) {
                rival3 = pieces[kingPos.colIndex][kingPos.rowIndex + 1];
            }
            if (kingPos.rowIndex - 1 >= 0) {
                rival4 = pieces[kingPos.colIndex][kingPos.rowIndex - 1];
            }
            if (kingPos.winningPosition()) {
                //player1 win the game.
                player1.setWins(player1.getWins() + 1);
                statInfo(player1);
                gameFinished = true;
                return true;
            }
            if (((rival1 != null && rival1.getOwner().equals(getSecondPlayer())) || kingPos.colIndex + 1 > 10) && ((rival2 != null && rival2.getOwner().equals(getSecondPlayer())) || kingPos.colIndex - 1 < 0) && ((rival3 != null && rival3.getOwner().equals(getSecondPlayer())) || kingPos.rowIndex + 1 > 10) && ((rival4 != null && rival4.getOwner().equals(getSecondPlayer())) || kingPos.rowIndex - 1 < 0)) {
                //player2 win the game.
                player2.setWins(player2.getWins() + 1);
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
            while (!deadPiece.isEmpty() && ! newL.isEmpty() && pieces[newL.peek().colIndex][newL.peek().rowIndex] == null)
            {
                //back from killing
                ConcretePiece piece = deadPiece.pop();
                if(piece instanceof Pawn)
                {
                    ((Pawn) piece).reduceKill();
                }
                pieces[newL.peek().colIndex][newL.pop().rowIndex] = deadPiece.pop();
            }
            if (!last.isEmpty()  &&! newL.isEmpty() && ! newL.equals(last.peek()))
            {
                //back to previous position.
                counterPos[newL.peek().colIndex][newL.peek().rowIndex] = counterPos[newL.peek().colIndex][newL.peek().rowIndex].substring(0,counterPos[newL.peek().colIndex][newL.peek().rowIndex].length()-3);
                pieces[newL.peek().colIndex][newL.peek().rowIndex].removeLastLoc();
                pieces[last.peek().colIndex][last.pop().rowIndex] = pieces[newL.peek().colIndex][newL.peek().rowIndex];
                pieces[newL.peek().colIndex][newL.pop().rowIndex] = null;
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
        String str = "";
        for(int i=0; i<11; i++)
        {
            for(int j=0; j<11; j++)
            {
                if (pieces[i][j] != null)
                {
                    pieces[i][j].setSquare();
                    print.add(pieces[i][j]);
                }
                if (counterPos[i][j] != null && getSize(counterPos[i][j]) > 1)
                {
                    str = "("+i+", "+j+")" +getSize(counterPos[i][j]) + " pieces";
                    piecesPrint.add(str);
                }
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
        //add the dead pieces to the sort.
        Stack<ConcretePiece> temp = new Stack<>();
        while(!deadPiece.isEmpty())
        {
            deadPiece.pop();
            if(!deadPiece.isEmpty())
            {
                temp.add(deadPiece.pop());
                print.add(temp.peek());
            }
        }
        //sort and print for section 2.
        Collections.sort(print, new MyComparator("kills",winner));
        for (int i = 0; i < print.size(); i++)
        {
            if (print.get(i) instanceof Pawn && ((Pawn) print.get(i)).getKills() > 0)
            {
                System.out.println(print.get(i).getName() + ": " + ((Pawn) print.get(i)).getKills() + " kills");
            }
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
            ConcretePiece killerPiece = pieces[newL.peek().colIndex][newL.peek().rowIndex];
            ArrayList<Position> enemyPos = killEnemy(killerPiece,newL.peek());
            for (int i=0; i< enemyPos.size(); i++)
            {
                if(killerPiece instanceof Pawn)
                {
                    ((Pawn) killerPiece).addKill();
                }
                //perform the kill.
                newL.add(new Position(enemyPos.get(i).colIndex,enemyPos.get(i).rowIndex));
                deadPiece.add(pieces[enemyPos.get(i).colIndex][enemyPos.get(i).rowIndex]);
                deadPiece.add(killerPiece);
                pieces[enemyPos.get(i).colIndex][enemyPos.get(i).rowIndex] = null;
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
        if (position.colIndex+1 <= 10) {
            rival1 = pieces[position.colIndex+1][position.rowIndex];
        }
        if(position.colIndex-1 >= 0) {
            rival2 = pieces[position.colIndex - 1][position.rowIndex];
        }
        if(position.rowIndex+1 <= 10) {
            rival3 = pieces[position.colIndex][position.rowIndex + 1];
        }
        if(position.rowIndex-1 >= 0) {
            rival4 = pieces[position.colIndex][position.rowIndex - 1];
        }
        if(rival1 != null && !rival1.getOwner().equals(piece.getOwner()) && !rival1.getType().equals("King"))
        {
            Position pos = new Position(position.colIndex+2,position.rowIndex);
            if((position.colIndex +2) <=10  && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.colIndex + 2][position.rowIndex];
                if (partner != null && !rival1.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.colIndex + 1, position.rowIndex));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.colIndex + 1, position.rowIndex));
            }
        }
        if(rival2 != null && !rival2.getOwner().equals(piece.getOwner()) && !rival2.getType().equals("King"))
        {
            Position pos = new Position(position.colIndex-2,position.rowIndex);
            if((position.colIndex-2) >= 0 && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.colIndex - 2][position.rowIndex];
                if (partner != null && !rival2.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.colIndex - 1, position.rowIndex));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.colIndex - 1, position.rowIndex));
            }
        }
        if(rival3 != null && !rival3.getOwner().equals(piece.getOwner()) && !rival3.getType().equals("King"))
        {
            Position pos = new Position(position.colIndex,position.rowIndex+2);
            if((position.rowIndex+2) <= 10 && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.colIndex][position.rowIndex + 2];
                if (partner != null && !rival3.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.colIndex, position.rowIndex + 1));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.colIndex, position.rowIndex + 1));
            }
        }
        if(rival4 != null && !rival4.getOwner().equals(piece.getOwner()) && !rival4.getType().equals("King"))
        {
            Position pos = new Position(position.colIndex, position.rowIndex-2);
            if((position.rowIndex-2) >= 0 && !pos.winningPosition())
            {
                ConcretePiece partner = pieces[position.colIndex][position.rowIndex-2];
                if (partner != null && !rival4.getOwner().equals(partner.getOwner()) && !partner.getType().equals("King"))
                {
                    //add to positions of pieces that going to die.
                    enemyPos.add(new Position(position.colIndex,position.rowIndex-1));
                }
            }
            else
            {
                //add to positions of pieces that going to die.
                enemyPos.add(new Position(position.colIndex,position.rowIndex-1));
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
