package TestClient;

import Server.Model.Player;
import TestClient.View.*;

import javax.swing.*;
import javax.swing.text.View;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static Player player;

    public static LoginForm loginForm;
    public static MainLobbyForm mainLobbyForm;
    public static OnlineList onlineList;
    public static Rankings rankings;
    public static RegisterForm registerForm;
    public static InGameForm inGameForm;
    public static GameNoticeFrm gameNoticeFrm;
    public static FindRoomFrm findRoomFrm;
    public static RoomListFrm roomListFrm;
    public static JoinRoomPasswordFrm joinRoomPasswordFrm;
    public static WaitingRoomFrm waitingRoomFrm;
    public static CreateRoomPasswordFrm createRoomPasswordFrm;

    public static ClientHandler clientHandler;

    public Client() {}

    public JFrame getVisibleFrame() {
        if(onlineList != null && onlineList.isVisible()) {
            return onlineList;
        }
        if (rankings != null && rankings.isVisible()) {
            return rankings;
        }
        return mainLobbyForm;
    }

    public static void openView(View viewName) {
        if (viewName != null) {
            switch (viewName) {
                case LOGIN:
                    loginForm = new LoginForm();
                    loginForm.setVisible(true);
                    break;
                case REGISTER:
                    registerForm = new RegisterForm();
                    registerForm.setVisible(true);
                    break;
                case HOMEPAGE:
                    mainLobbyForm = new MainLobbyForm();
                    mainLobbyForm.setVisible(true);
                    break;
                case ROOM_LIST:
                    roomListFrm = new RoomListFrm();
                    roomListFrm.setVisible(true);
                    break;
                case FIND_ROOM:
                    findRoomFrm = new FindRoomFrm();
                    findRoomFrm.setVisible(true);
                    break;
                case WAITING_ROOM:
                    waitingRoomFrm = new WaitingRoomFrm();
                    waitingRoomFrm.setVisible(true);
                    break;
                case CREATE_ROOM_PASSWORD:
                    createRoomPasswordFrm = new CreateRoomPasswordFrm();
                    createRoomPasswordFrm.setVisible(true);
                    break;
                case RANK:
                    rankings = new Rankings();
                    rankings.setVisible(true);
                    break;
                case ONLINE:
                    onlineList = new OnlineList();
                    onlineList.setVisible(true);
                    break;
            }
        }
    }

    public static void openView(View viewName, int arg1, String arg2) {
        if (viewName != null) {
            if (viewName == View.JOIN_ROOM_PASSWORD) {
                joinRoomPasswordFrm = new JoinRoomPasswordFrm(arg1, arg2);
                joinRoomPasswordFrm.setVisible(true);
            }
        }
    }

    public static void openView(View viewName, Player competitor, String competitorIP, int room_ID) {
        if (viewName == View.GAME_CLIENT) {
            inGameForm = new InGameForm(competitor, competitorIP, room_ID);
            inGameForm.setVisible(true);
        }
    }


    public static void openView(View viewName, String arg1, String arg2) {
        if (viewName != null) {
            switch (viewName) {
                case GAME_NOTICE:
                    gameNoticeFrm = new GameNoticeFrm(arg1, arg2);
                    gameNoticeFrm.setVisible(true);
                    break;
                case LOGIN:
                    loginForm = new LoginForm(arg1, arg2);
                    loginForm = new LoginForm();
                    loginForm.setVisible(true);
            }
        }
    }

    public static void closeView(View viewName) {
        if (viewName != null) {
            switch (viewName) {
                case LOGIN:
                    loginForm.dispose();
                    break;
                case REGISTER:
                    registerForm.dispose();
                    break;
                case HOMEPAGE:
                    mainLobbyForm.dispose();
                    break;
                case ROOM_LIST:
                    roomListFrm.dispose();
                    break;
                case FIND_ROOM:
                    findRoomFrm.stopAllThread();
                    findRoomFrm.dispose();
                    break;
                case WAITING_ROOM:
                    waitingRoomFrm.dispose();
                    break;
                case GAME_CLIENT:
                    inGameForm.stopAllThread();
                    inGameForm.dispose();
                    break;
                case CREATE_ROOM_PASSWORD:
                    createRoomPasswordFrm.dispose();
                    break;
                case JOIN_ROOM_PASSWORD:
                    joinRoomPasswordFrm.dispose();
                    break;
                case RANK:
                    rankings.dispose();
                    break;
                case GAME_NOTICE:
                    gameNoticeFrm.dispose();
                    break;
                case ONLINE:
                    onlineList.dispose();
                    break;
            }

        }
    }

    public static void closeAllViews() {
        if (loginForm != null) {
            loginForm.dispose();
        }
        if (registerForm != null) {
            registerForm.dispose();
        }
        if (mainLobbyForm != null) {
            mainLobbyForm.dispose();
        }
        if (onlineList != null) {
            onlineList.stopAllThread();
            onlineList.dispose();
        }
        if (rankings != null) {
            rankings.dispose();
        }
        if (inGameForm != null) {
            inGameForm.stopAllThread();
            inGameForm.dispose();
        }
        if (gameNoticeFrm != null) {
            gameNoticeFrm.dispose();
        }
        if (findRoomFrm != null) {
            findRoomFrm.stopAllThread();
            findRoomFrm.dispose();
        };
        if (roomListFrm != null) {
            roomListFrm.dispose();
        };
        if (joinRoomPasswordFrm != null) {
            joinRoomPasswordFrm.dispose();
        };
        if (waitingRoomFrm != null) {
            waitingRoomFrm.dispose();
        };
        if (createRoomPasswordFrm != null) {
            createRoomPasswordFrm.dispose();
        }
    }

    public static void main(String[] args) {
        new Client().initView();
    }

    public void initView() {
        loginForm = new LoginForm();
        loginForm.setVisible(true);
        clientHandler = new ClientHandler();
        clientHandler.start();
    }

    public enum View {
        LOGIN,
        REGISTER,
        HOMEPAGE,
        ROOM_LIST,
        FIND_ROOM,
        WAITING_ROOM,
        GAME_CLIENT,
        CREATE_ROOM_PASSWORD,
        JOIN_ROOM_PASSWORD,
        COMPETITOR_INFO,
        RANK,
        GAME_NOTICE,
        ROOM_NAME_FRM,
        ONLINE
    }
}
