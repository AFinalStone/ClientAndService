import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class Client {

    // private Button btn_login = null;
    private static final String HOST = "192.168.8.11";
    private static final int PORT = 9999;
    private static Socket socket = null;
    private static BufferedReader in = null;
    private static PrintWriter out = null;
    private static String content = "";

    public static void main(String[] args) throws IOException {
        connectServer();
    }

    private static void connectServer() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
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
