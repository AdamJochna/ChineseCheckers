import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Thread {

    BufferedReader in;
    PrintWriter out;
    String nick;
    Socket socket;

    public ChatClient(String ip) throws IOException {
            this.socket = new Socket(ip, 9001);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        while(true){
            try {
                String line = in.readLine();
                System.out.println(line);

                if (line.startsWith("UPDATE")){
                    GUI.getInstance().updateView(line);
                }
                if (line.startsWith("TURN")){
                    GUI.getInstance().updateTurn(line);
                }
            }
            catch(Exception e){System.out.println(e);}
        }
    }

    public void sendMove(int x1GC,int y1GC,int x2GC,int y2GC){
        if(nick!=null)
            out.println("MOVE "+nick+" "+Integer.toString(x1GC)+" "+Integer.toString(y1GC)+" "+Integer.toString(x2GC)+" "+Integer.toString(y2GC));
    }

    public void setNick(String s){
        out.println("TRYSUBMIT "+ s);
        nick=s;
    }
}