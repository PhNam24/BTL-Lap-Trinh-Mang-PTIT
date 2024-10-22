package Server.Controller;

import Server.DAO.GameMatchDAO;
import Server.DAO.PlayerDAO;
import Server.DAO.ProductDAO;
import Server.Model.GameMatch;
import Server.Model.Product;
import Server.Server;

import java.io.IOException;

public class Room {
    private final int id;
    private final ServerThread player1;
    private ServerThread player2;
    private double p1Price;
    private double p2Price;
    private Product product;
    private String password;
    private boolean isDrawHandle = false;
    private boolean isSetProduct = false;
    private final PlayerDAO playerDAO;
    private final ProductDAO productDAO;
    private final GameMatchDAO gameMatchDAO;

    public Room(ServerThread player1) {
        System.out.println("Tạo phòng thành công, ID là: " + Server.ROOM_ID);
        this.id = Server.ROOM_ID++;
        this.player1 = player1;
        this.player2 = null;
        this.password = " ";
        playerDAO = new PlayerDAO();
        productDAO = new ProductDAO();
        gameMatchDAO = new GameMatchDAO();
    }

    public int getNumberOfPlayer() {
        return player2 == null ? 1 : 2;
    }

    public void broadCast(String message) {
        try {
            player1.write(message);
            player2.write(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int getCompetitorID(int ID_ClientNumber) {
        if (player1.getClientNumber() == ID_ClientNumber)
            return player2.getPlayer().getId();
        return player1.getPlayer().getId();
    }

    public ServerThread getCompetitor(int ID_ClientNumber) {
        if (player1.getClientNumber() == ID_ClientNumber)
            return player2;
        return player1;
    }

    public void setPlayersToPlaying() {
        playerDAO.updateToPlaying(player1.getPlayer().getId());
        if (player2 != null) {
            playerDAO.updateToPlaying(player2.getPlayer().getId());
        }
    }

    public void setPlayersToNotPlaying() {
        playerDAO.updateToNotPlaying(player1.getPlayer().getId());
        if (player2 != null) {
            playerDAO.updateToNotPlaying(player2.getPlayer().getId());
        }
    }

    public void setPlayersPrice(int id, double price) {
        if(player1.getPlayer().getId() == id) {
            p1Price = price;
        }
        else {
            p2Price = price;
        }
    }

    public int getResult() {
        System.out.println(p1Price + "-" + p2Price);
        if ((p1Price == p2Price) || (p1Price > product.getPrice() && p2Price > product.getPrice())) {
            return -1;
        }
        if (p1Price <= product.getPrice() && p2Price > product.getPrice()) {
            return player1.getPlayer().getId();
        }
        if (p2Price <= product.getPrice() && p1Price > product.getPrice()) {
            return player2.getPlayer().getId();
        }
        return p1Price > p2Price ? player1.getPlayer().getId() : player2.getPlayer().getId();
    }

    public void saveGameMatch(GameMatch gameMatch) {
        gameMatchDAO.addGameMatch(gameMatch);
    }

    public int getId() {
        return id;
    }

    public ServerThread getPlayer1() {
        return player1;
    }

    public ServerThread getPlayer2() {
        return player2;
    }

    public Product getProduct() {return product;}

    public boolean isSetProduct() {return isSetProduct;}

    public boolean isDrawHandle() {
        return isDrawHandle;
    }

    public void setPlayer2(ServerThread player2) {
        this.player2 = player2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProduct() {
        this.product = productDAO.getRandomProduct();
    }

    public void setDrawHandle(boolean drawHandle) {
        this.isDrawHandle = drawHandle;
    }

    public void setSetProduct(boolean setProduct) {
        this.isSetProduct = setProduct;
    }
}
