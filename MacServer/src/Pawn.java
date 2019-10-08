import java.util.ArrayList;

public class Pawn {
    int x;
    int y;
    int k;

    public Pawn(int x,int y,int k)
    {
        this.x=x;
        this.y=y;
        this.k=k;
    }

    public void setCoords(int xnew,int ynew) {
        x=xnew;
        y=ynew;
    }

    public static ArrayList<Pawn> Triangle(int x,int y,boolean isUp,int k){
        ArrayList<Pawn> pawnstriangle= new ArrayList<>();

        if(isUp==false) {
            for(int i=0;i<4;i++)
                for(int j=0;j<=i;j++)
                    pawnstriangle.add(new Pawn(i+x,y-j,k));
        }
        else {
            for(int i=0;i<4;i++)
                for(int j=3-i;j>=0;j--)
                    pawnstriangle.add(new Pawn(i+x,j+y,k));
        }
        return pawnstriangle;
    }

    public static ArrayList<Pawn> PlayerTriangle(int i){
        ArrayList<Pawn> tmp = new ArrayList<>();
        switch (i) {
            case 0: tmp.addAll(Triangle(-4,5,true,0));
                break;
            case 1: tmp.addAll(Triangle(1,4,false,1));
                break;
            case 2: tmp.addAll(Triangle(5,-4,true,2));
                break;
            case 3: tmp.addAll(Triangle(1,-5,false,3));
                break;
            case 4: tmp.addAll(Triangle(-4,-4,true,4));
                break;
            case 5: tmp.addAll(Triangle(-8,4,false,5));
                break;
        }
        return tmp;
    }

    public static ArrayList<Pawn> AllowedPawns(){
        ArrayList<Pawn> allowed = new ArrayList<>();

        allowed.addAll(Triangle(1,4,false,0));
        allowed.addAll(Triangle(1,-5,false,0));
        allowed.addAll(Triangle(-8,4,false,0));

        for(int i=0;i<=12;i++){
            for(int j=0;j<=12-i;j++){
                allowed.add(new Pawn(j-4,i-4,0));
            }
        }

        return allowed;
    }
}
