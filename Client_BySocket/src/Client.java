import java.io.*;
import java.net.Socket;

public class Client {

    // private Button btn_login = null;
    private static final String HOST = "192.168.8.11";
    private static final int PORT = 9999;
    private static Socket socket = null;
    private static BufferedReader in = null;
    private static PrintWriter out = null;
    private static String content = "";

    public static void main(String[] args) {
        connectServer();
    }

    private static void connectServer(){
        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket(HOST, PORT);
                    in = new BufferedReader(new InputStreamReader(socket
                            .getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                    //启动线程，接收服务器发送过来的数据
                    while (true) {
                        if (!socket.isClosed()) {
                            if (socket.isConnected()) {
                                if (!socket.isInputShutdown()) {
                                    if ((content = in.readLine()) != null) {
                                        content += "\n";
                                        System.out.println(content);
                                    } else {

                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private static void sendMessage(final String msg){
        new Thread(){
            @Override
            public void run() {
                if (socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println(msg);
                    }
                }
            }
        }.start();

    }
}
