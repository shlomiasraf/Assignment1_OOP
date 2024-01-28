public class ConcretePlayer implements Player{
    int playerNumber;
    int playerWins;
    public ConcretePlayer(int playerNumber,int playerWins)
    {
        this.playerNumber = playerNumber;
        this.playerWins = playerWins;
    }

    /**
     * @brief checks if the objects are equals.
     * @param player the object we want to compare.
     * @return true if the objects are equals.
     */
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

    /**
     *
     * @return the number of this player.
     */
    public int getPlayerNumber()
    {
        return this.playerNumber;
    }

    /**
     *
     * @return true if this player is player1.
     */
    @Override
    public boolean isPlayerOne()
    {
        if(playerNumber == 1)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @return the number of wins of this player.
     */
    @Override
    public int getWins()
    {
        return this.playerWins;
    }

    /**
     * @brief update the number of wins.
     * @param wins the wins we want to update to.
     */
    public void setWins(int wins)
    {
        this.playerWins = wins;
    }
}
