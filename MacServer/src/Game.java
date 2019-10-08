import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Game{
    static final Game game = new Game();
    ArrayList<Pawn> board = new ArrayList<>();
    ArrayList<Pawn> allowedpositions;
    String[] nicks = new String[6];
    boolean gamestarted;
    int timeleft;
    int i;
    int turn;
    Timer timer;
    Random generator;
    String stringboard;

    private Game(){
        this.stringboard="";
        this.generator=new Random();
        this.timeleft=30;
        this.gamestarted=false;
        this.i=0;
        this.turn=0;
        this.allowedpositions=Pawn.AllowedPawns();
        this.timer=new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                System.out.println("TIMETOSTART " + timeleft);
                timeleft--;

                if(timeleft==0) {
                    System.out.println("GAME STARTS");
                    addBots();
                    gamestarted = true;
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public static Game getInstance(){
        return game;
    }

    public void addPlayer(String player){
        if(isAlreadyInGame(player)==false && !gamestarted){
            Integer[] playerindex = {0,3,1,4,2,5};
            nicks[playerindex[i]]=player;
            board.addAll(Pawn.PlayerTriangle(playerindex[i]));
            encodePawnsToString();
            i++;
        }
    }

    public void addBots(){
        int k=i;
        for(int j=k;j<6;j++){
            String botname="bot".concat(Integer.toString(j));
            addPlayer(botname);
        }
    }


    public void setNextTurn(){
        turn++;
        turn=turn%6;
    }

    public boolean isAlreadyInGame(String player){
        for(int i=0;i<6;i++){
            if(nicks[i]!=null && nicks[i].equals(player))
                return true;
        }
        return false;
    }

    public void encodePawnsToString(){
        String s="UPDATE";
        for (Pawn p: board)
            s=s.concat(" "+Integer.toString(p.x)+" "+Integer.toString(p.y)+" "+Integer.toString(p.k));
        stringboard=s;
    }

    public int getNickIndex(String nick) {
        for(int i=0;i<6;i++){
            if(nicks[i]!=null && nicks[i].equals(nick))
                return i;
        }

        return -1;
    }

    public void tryToMakeMove(String move){
        String[] splited = move.split("\\s+");

        int userindex=getNickIndex(splited[1]);
        int x1=Integer.parseInt(splited[2]);
        int y1=Integer.parseInt(splited[3]);
        int x2=Integer.parseInt(splited[4]);
        int y2=Integer.parseInt(splited[5]);

        ArrayList<Integer> winners=detectWinners();

        if(winners.contains(userindex)){
            setNextTurn();
            makeBotMove();
        }
        else{
            if(getPawnOnPos(x1,y1)!=null && getPawnOnPos(x1,y1).k == userindex && turn==userindex && isPosAllowed(x2,y2) && !isTryingToEscapeFromFinalTriangle(x1,y1,x2,y2,userindex) && gamestarted){
                if (isMoveByOne(x1,y1,x2,y2) && !isPawnOnPos(x2,y2)){
                    getPawnOnPos(x1, y1).setCoords(x2, y2);
                    encodePawnsToString();
                    setNextTurn();
                    makeBotMove();
                }
                else if(isMoveByTwo(x1,y1,x2,y2) && isPawnOnPos((x1+x2)/2,(y1+y2)/2) && !isPawnOnPos(x2,y2)){
                    getPawnOnPos(x1, y1).setCoords(x2, y2);
                    encodePawnsToString();
                    setNextTurn();
                    makeBotMove();
                }
                else if(x1==x2 && y1==y2){
                    setNextTurn();
                    makeBotMove();
                }
            }
        }
    }


    public boolean isMoveByOne(int x1,int y1,int x2,int y2){
        return isMoveByTwo(2*x1,2*y1,2*x2,2*y2);
    }

    public boolean isMoveByTwo(int x1,int y1,int x2,int y2){
        if(x2-x1==0 && (y2-y1)*(y2-y1)==4){
            return true;
        } else if(y2-y1==0 && (x2-x1)*(x2-x1)==4){
            return true;
        } else if(x2-x1==-2 && y2-y1==2){
            return true;
        } else if(x2-x1==2 && y2-y1==-2){
            return true;
        } else{
            return false;
        }
    }

    public boolean isPawnOnPos(int x,int y){
        for(Pawn p : board){
            if(p.x==x && p.y==y)
                return true;
        }
        return false;
    }

    public Pawn getPawnOnPos(int x,int y){
        for(Pawn p : board){
            if(p.x==x && p.y==y)
                return p;
        }
        return null;
    }

    public boolean isPosAllowed(int x,int y){
        for(Pawn p : allowedpositions){
            if(p.x==x && p.y==y)
                return true;
        }
        return false;
    }

    public boolean isTryingToEscapeFromFinalTriangle(int x1,int y1,int x2,int y2,int k){
        ArrayList<Pawn> finaltriangle = Pawn.PlayerTriangle((k+3)%6);

        boolean firstpawnintriangle=false;
        boolean secondpawnintriangle=false;

        for(Pawn p : finaltriangle){
            if(p.x==x1 && p.y==y1)
                firstpawnintriangle=true;
        }

        for(Pawn p : finaltriangle){
            if(p.x==x2 && p.y==y2)
                secondpawnintriangle=true;
        }

        if(firstpawnintriangle==true && secondpawnintriangle==false){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Integer> detectWinners(){
        ArrayList<Integer> winners = new ArrayList<>();
        for(int i=0;i<6;i++){
            if(isPlayerInWinningPosition(i))
                winners.add(i);
        }

        return winners;
    }

    public boolean isPlayerInWinningPosition(int k){
        ArrayList<Pawn> finaltriangle = Pawn.PlayerTriangle((k+3)%6);
        boolean win=true;

        for(Pawn p : finaltriangle){
            if(getPawnOnPos(p.x,p.y)==null || getPawnOnPos(p.x,p.y).k!=k)
                win=false;
        }

        return win;
    }

    public void makeBotMove(){
        if(nicks[turn].startsWith("bot")){
            int initturn=turn;
            int counter=0;

            while(initturn==turn){
                counter++;
                Pawn randompawn=randomPawn();
                String randommove=randomMove(randompawn,nicks[turn]);

                if(randompawn.k==turn)
                {
                    if(counter<8000 && decreasesDistance(randommove,turn)){
                        tryToMakeMove(randommove);
                    }
                    else if(counter>=8000){
                        tryToMakeMove(randommove);
                    }
                }

                if(counter==8500){
                    setNextTurn();
                    makeBotMove();
                    break;
                }
            }
        }
    }

    public boolean decreasesDistance(String randommove,int turn){
        String[] splited = randommove.split("\\s+");
        Pawn destination=destinationPawn(turn);
        double x1=(double)(Integer.parseInt(splited[2]));
        double y1=(double)(Integer.parseInt(splited[3]));
        double x2=(double)(Integer.parseInt(splited[4]));
        double y2=(double)(Integer.parseInt(splited[5]));

        double yPix1=300 - y1*24.75;
        double xPix1=300 + x1*28.5833333 + y1*(28.5833333/2);
        double yPix2=300 - y2*24.75;
        double xPix2=300 + x2*28.5833333 + y2*(28.5833333/2);
        double ydestPix=300 - destination.y*24.75;
        double xdestPix=300 + destination.x*28.5833333 + destination.y*(28.5833333/2);

        double dist1=Math.sqrt(Math.pow(xdestPix-xPix1,2)+Math.pow(ydestPix-yPix1,2));
        double dist2=Math.sqrt(Math.pow(xdestPix-xPix2,2)+Math.pow(ydestPix-yPix2,2));

        if(dist2<dist1){
            return true;
        }
        else{
            return false;
        }
    }

    public String randomMove(Pawn pawn,String nick){
        int dx=0;
        int dy=0;
        int dir=generator.nextInt(6);
        int dist=generator.nextInt(2)+1;

        switch (dir) {
            case 0: dx=0;dy=dist;
                break;
            case 1: dx=0;dy=-dist;
                break;
            case 2: dx=dist;dy=0;
                break;
            case 3: dx=-dist;dy=0;
                break;
            case 4: dx=-dist;dy=dist;
                break;
            case 5: dx=dist;dy=-dist;
                break;
        }

        Pawn updated=new Pawn(pawn.x+dx,pawn.y+dy,pawn.k);
        String s="MOVE "+nick+" "+Integer.toString(pawn.x)+" "+Integer.toString(pawn.y)+" "+Integer.toString(updated.x)+" "+Integer.toString(updated.y);
        return s;
    }

    public Pawn randomPawn(){
        int i = generator.nextInt(60);
        return board.get(i);
    }

    public Pawn destinationPawn(int k){
        Pawn p=new Pawn(0,0,0);

        switch (k) {
            case 0: p=new Pawn(4,-8,0);
                break;
            case 1: p=new Pawn(-4,-4,1);
                break;
            case 2: p=new Pawn(-8,4,2);
                break;
            case 3: p=new Pawn(-4,8,3);
                break;
            case 4: p=new Pawn(4,4,4);
                break;
            case 5: p=new Pawn(8,-4,5);
                break;
        }
        return p;
    }
}