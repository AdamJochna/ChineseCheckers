import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashSet;
import java.net.Socket;

public class Main{

    private static final int PORT = 9001;
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private String line;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private Game game;
        private String lastboard;

        public Handler(Socket socket) {
            this.socket = socket;
            this.game = Game.getInstance();
            this.lastboard ="";
        }

        public void run()
        {
            try
            {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    line = in.readLine();

                    System.out.println("RECIVED:" + line);

                    if (line.startsWith("TRYSUBMIT")) {
                        String[] splited = line.split("\\s+");
                        name=splited[1];

                        synchronized (names) {
                            if (!names.contains(name) && !name.startsWith("bot")) {
                                names.add(name);
                                break;
                            }
                        }
                    }
                }

                game.addPlayer(name);
                out.println("NAMEACCEPTED");
                writers.add(out);

                for (PrintWriter writer : writers) {
                    writer.println(game.stringboard);
                }

                while (true) {
                    if(!lastboard.equals(game.stringboard)){
                        lastboard=game.stringboard;

                        for (PrintWriter writer : writers) {
                            writer.println(game.stringboard);
                        }
                    }

                    String input = in.readLine();
                    System.out.println("RECIVED:" + input);

                    if (input.startsWith("MOVE")){
                        game.tryToMakeMove(input);

                        if (input == null){
                            return;
                        }
                    }

                    for (PrintWriter writer : writers) {
                        writer.println("TURN "+game.nicks[game.turn]);
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            finally
            {
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                }
                catch(IOException e){}
            }
        }
    }
}