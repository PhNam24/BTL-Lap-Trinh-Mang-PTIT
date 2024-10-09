package Server.Model;

public class GameMatch {
    private int id;
    private int player1ID;
    private int player2ID;
    private int winnerID;
    private int productID;

    public GameMatch(int id, int player1ID, int player2ID, int winnerID, int productID) {
        this.id = id;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.winnerID = winnerID;
        this.productID = productID;
    }

    public GameMatch(int player1ID, int player2ID, int winnerID, int productID) {
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.winnerID = winnerID;
        this.productID = productID;
    }

    public GameMatch(GameMatch gameMatch) {
        this.id = gameMatch.id;
        this.player1ID = gameMatch.player1ID;
        this.player2ID = gameMatch.player2ID;
        this.winnerID = gameMatch.winnerID;
        this.productID = gameMatch.productID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer1ID() {
        return player1ID;
    }

    public void setPlayer1ID(int player1ID) {
        this.player1ID = player1ID;
    }

    public int getPlayer2ID() {
        return player2ID;
    }

    public void setPlayer2ID(int player2ID) {
        this.player2ID = player2ID;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(int winnerID) {
        this.winnerID = winnerID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
