package NetChat.Server;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class Server {
    private Vector<ClientHandler> clients;


    public Server() throws SQLException {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            AuthService.connect();
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    public void broadcastMsg(ClientHandler reciever, String msg) {
        for (ClientHandler o: clients) {
            if (!o.checkBlackList(reciever.getNick())){
                o.sendMsg(reciever.getNick() + ": " + msg);
            }
        }
    }

    public void sendPrivateMessage(ClientHandler sender, String reciever, String msg){
        for (ClientHandler o: clients) {
            if (o.getNick().equals(reciever)){
                o.sendMsg("Отправитель " + sender.getNick() + ": " + msg);
                sender.sendMsg("Получателю " + reciever + ": " + msg);
                return;
            }
        }
        sender.sendMsg("Клиент с ником " + reciever + " не найден в чате.");
    }

    public void subscribe(ClientHandler client){
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client){
        clients.remove(client);
    }


    public boolean isOnline(String nickname) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nickname)) {
                return true;
            }
        }
        return false;
    }
}