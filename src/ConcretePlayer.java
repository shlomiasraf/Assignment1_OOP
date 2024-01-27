public class ConcretePlayer implements Player{
    int playerNumber;
    int playerWins;
    public ConcretePlayer(int playerNumber,int playerWins)
    {
        this.playerNumber = playerNumber;
        this.playerWins = playerWins;
    }

    @Override
    public boolean equals(Object player)
    {
        ConcretePlayer player1 = (ConcretePlayer) player;
        if(player1.getPlayerNumber() == getPlayerNumber() && player1.getWins() == getWins())
        {
            return true;
        }
        return false;
    }
    public int getPlayerNumber()
    {
        return this.playerNumber;
    }
    @Override
    public boolean isPlayerOne()
    {
        if(playerNumber == 1)
        {
            return true;
        }
        return false;
    }
    @Override
    public int getWins()
    {
        return this.playerWins;
    }
    public void setWins(int wins)
    {
        this.playerWins = wins;
    }
}
