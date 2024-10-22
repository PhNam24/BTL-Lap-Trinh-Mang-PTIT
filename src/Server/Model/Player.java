package Server.Model;

public class Player {
    private int id;
    private String username;
    private String password;
    private String nickName;
    private String avatar;
    private int win = 0;
    private int lose = 0;
    private int draw = 0;
    private double score = 0;
    private boolean isOnline;
    private boolean isPlaying;

    public Player() {}

    public Player(int id, String username, String password, String nickName, String avatar, int win, int lose, int draw, double score, boolean isOnline, boolean isPlaying) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.avatar = avatar;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
        this.score = score;
        this.isOnline = isOnline;
        this.isPlaying = isPlaying;
    }

    public Player(int id, String username, String password, String nickName, String avatar, int win, int lose, int draw, double score) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.avatar = avatar;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
        this.score = score;
    }

    public Player(Player player) {
        this.id = player.id;
        this.username = player.username;
        this.password = player.password;
        this.nickName = player.nickName;
        this.avatar = player.avatar;
        this.win = player.win;
        this.lose = player.lose;
        this.draw = player.draw;
        this.score = player.score;
        this.isOnline = player.isOnline;
        this.isPlaying = player.isPlaying;
    }

    public Player(int id, String username, String nickName, boolean isOnline, boolean isPlaying) {
        this.id = id;
        this.username = username;
        this.nickName = nickName;
        this.isOnline = isOnline;
        this.isPlaying = isPlaying;
    }

    public Player(String username, String password, String nickName, String avatar) {
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.avatar = avatar;
    }

    public Player(String username, String nickName, String avatar) {
        this.username = username;
        this.nickName = nickName;
        this.avatar = avatar;
    }

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.valueOf(id) + ',' + username + ',' + password + ',' + nickName + ','
                + avatar + ',' + win + ',' + lose + ',' + draw + ',' + score;
    }

    public int getNumberOfGame() {
        return this.win + this.lose + this.draw;
    }

    public String getWinRate() {
        return getNumberOfGame() > 0 ? String.valueOf(this.win * 100.0 / this.getNumberOfGame()) + "%" : "0%";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
