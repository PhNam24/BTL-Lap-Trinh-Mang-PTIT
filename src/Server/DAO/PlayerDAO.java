package Server.DAO;

import Server.Model.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerDAO extends DAO {
    public PlayerDAO() {
        super();
    }

    // Kiểm tra đăng nhập
    public Player verifyPlayer(Player player) {
        try {
            String query = "SELECT * FROM player WHERE username = ? AND password = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, player.getUsername());
            stm.setString(2, player.getPassword());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Player(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getDouble(9),
                        (rs.getInt(10) != 0),
                        (rs.getInt(11) != 0)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra tài khoản đã tồn tại
    public boolean checkDuplicated(String username) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM player WHERE username = ?");
            preparedStatement.setString(1, username);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tạo tài khoản mới
    public void addPlayer(Player player) {
        try {
            String query = "INSERT INTO player(username, password, nickName, avatar) VALUES(?,?,?,?) ";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, player.getUsername());
            stm.setString(2, player.getPassword());
            stm.setString(3, player.getNickName());
            stm.setString(4, player.getAvatar());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sửa thông tin tài khoản
    public void updatePlayer(Player player) {
        try {
            String query = "UPDATE player SET nickName = ?, avatar = ? WHERE username = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, player.getNickName());
            stm.setString(2, player.getAvatar());
            stm.setString(3, player.getUsername());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá tài khoản
    public void deletePlayer(Player player) {
        try {
            String query = "DELETE FROM player WHERE username = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, player.getUsername());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy nickname theo id
    public String getPlayerNickNameByID(int id) {
        try {
            String query = "SELECT player.nickName FROM player WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả người chơi
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        try {
            String query = "SELECT * FROM player";
            PreparedStatement stm = con.prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                players.add(new Player(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getDouble(9),
                        (rs.getInt(10) != 0),
                        (rs.getInt(11) != 0)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    // Tìm người chơi theo keyword
    public ArrayList<Player> searchPlayers(String keyword) {
        ArrayList<Player> players = new ArrayList<>();
        try {
            String query = "SELECT * FROM player WHERE nickName LIKE ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, "%" + keyword + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                players.add(new Player(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getDouble(9),
                        (rs.getInt(10) != 0),
                        (rs.getInt(11) != 0)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    // Số trận thắng
    public int getWin(int id) {
        try {
            String query = "SELECT player.win FROM player WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Xử lý data khi chiến thắng
    public void addWin(int id) {
        try {
            String query = "UPDATE player SET win = ?, score = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, getWin(id) + 1);
            stm.setDouble(2, getScore(id) + 1);
            stm.setInt(3, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Số trận thua
    public int getLose(int id) {
        try {
            String query = "SELECT player.lose FROM player WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Xử lý data khi thua
    public void addLose(int id) {
        try {
            String query = "UPDATE player SET lose = ?, score = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, getLose(id) + 1);
            stm.setDouble(2, getScore(id) - 0.5);
            stm.setInt(3, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Số trận hoà
    public int getDraw(int id) {
        try {
            String query = "SELECT player.draw FROM player WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Xử lý data khi hoà
    public void addDraw(int id) {
        try {
            String query = "UPDATE player SET draw = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, getDraw(id) + 1);
            stm.setInt(2, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Điểm của người chơi
    public double getScore(int id) {
        try {
            String query = "SELECT player.score FROM player WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Lấy bảng xếp hạng theo điểm > win > draw > lose
    public ArrayList<Player> getLeaderBoard() {
        ArrayList<Player> leaderBoard = new ArrayList<>();
        try {
            String query = "SELECT * FROM player ORDER BY player.score DESC , player.win DESC, player.draw DESC, player.lose ASC;";
            PreparedStatement stm = con.prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                leaderBoard.add(new Player(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getDouble(9)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderBoard;
    }

    // Lấy danh sách người chơi online
    public ArrayList<Player> getOnlineList() {
        ArrayList<Player> onlineList = new ArrayList<>();
        try {
            String query = "SELECT * FROM player WHERE isOnline = 1";
            PreparedStatement stm = con.prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                onlineList.add(new Player(
                        rs.getString(2),
                        rs.getString(4),
                        rs.getString(5)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onlineList;
    }


    // Cập nhật trạng thái online
    public void updateToOnline(int ID) {
        try {
            String query = "UPDATE player SET isOnline = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, 1);
            stm.setInt(2, ID);
            System.out.println(stm);
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateToOffline(int ID) {
        try {
            String query = "UPDATE player SET isOnline = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, 0);
            stm.setInt(2, ID);
            System.out.println(stm);
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateToPlaying(int ID) {
        try {
            String query = "UPDATE player SET isPlaying = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, 1);
            stm.setInt(2, ID);
            System.out.println(stm);
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateToNotPlaying(int ID) {
        try {
            String query = "UPDATE player SET isPlaying = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, 0);
            stm.setInt(2, ID);
            System.out.println(stm);
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
