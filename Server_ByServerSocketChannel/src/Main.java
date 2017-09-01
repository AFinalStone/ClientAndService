import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    private static final int PORT = 9999;
    private List<Socket> mList = new ArrayList<Socket>();
    private ExecutorService mExecutorService = null; //thread pool

    public static void main(String[] args) {
        new Main();
    }
    public Main() {
        try {
            mExecutorService = Executors.newCachedThreadPool();  //create a thread pool
            System.out.println("服务器已启动...");
            Socket client = null;
            while(true) {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.socket().bind(new InetSocketAddress(9999));
                while(true){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    mExecutorService.execute(new Service(socketChannel));
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    class Service implements Runnable {
        private SocketChannel socketChannel;
        private BufferedReader in = null;
        private String msg = "";

        public Service(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            try {
                ByteBuffer buf = ByteBuffer.allocate(48);

                int bytesRead = socketChannel.read(buf);
                while (bytesRead != -1) {
                    System.out.println("Read " + bytesRead);
                    buf.flip();
                    while(buf.hasRemaining()){
                        System.out.print((char) buf.get());
                    }
                    buf.clear();
                    bytesRead = socketChannel.read(buf);
                }
                //客户端只要一连到服务器，便向客户端发送下面的信息。
            msg = "服务器地址：http:/" +this.socketChannel.getLocalAddress() + "come toal:"
                        +mList.size()+"（服务器发送）";
                this.sendmsg();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            try {
                while(true) {
                    if((msg = in.readLine())!= null) {
                        msg = "http:/"+socketChannel.getLocalAddress() + ":" + msg+"（服务器发送）";
                        this.sendmsg();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 循环遍历客户端集合，给每个客户端都发送信息。
         */
        public void sendmsg() {
            System.out.println(msg);
            int num =mList.size();
            for (int index = 0; index < num; index ++) {
                Socket mSocket = mList.get(index);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(mSocket.getOutputStream())),true);
                    pout.println(msg);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
