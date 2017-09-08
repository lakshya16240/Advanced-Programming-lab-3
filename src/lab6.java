import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

public class lab6 {

    public static void main(String[] args ) throws IOException {

        Read.init(System.in);
        int numberOfKnights, iterations, xQueen, yQueen;
        int flag=0;
        numberOfKnights=Read.nextInt();
        iterations = Read.nextInt();
        xQueen = Read.nextInt();
        yQueen = Read.nextInt();

        PrintWriter writer=new PrintWriter("./src/answer.txt", "UTF-8");

        Knight[] knights = new Knight[numberOfKnights];
        for(int i=0;i<numberOfKnights;i++){

            InputStream inputStream = new FileInputStream("/Users/GhanshamBansal/Desktop/Lab 6/Test Case/Input/" + (i+1) + ".txt");
            Read.init(inputStream);
            String s = Read.next();
            int xKnight = Read.nextInt();
            int yKnight = Read.nextInt();
            int m = Read.nextInt();
            knights[i] = new Knight(s, xKnight, yKnight, false, true);
            for(int j=0;j<m;j++){
                s  = Read.next();
                if(s.equalsIgnoreCase("Coordinate")){
                    knights[i].getSt().push(new Coordinate(Read.nextInt(),Read.nextInt()));
                }
                else{
                    knights[i].getSt().push(Read.next());
                }
            }

        }
        Arrays.sort(knights);

        for(int i=1; i<=iterations;i++){

            for(int j=0;j<numberOfKnights;j++){

                if(knights[j].isInGrid()) {
                    try {

                        writer.println(i + " " + knights[j].getName() + " " + knights[j].getxKnight() + " " + knights[j].getyKnight());
                        Coordinate coordinate = (Coordinate) knights[j].remove();
                        knights[j].setxKnight(coordinate.getX());
                        knights[j].setyKnight(coordinate.getY());
                        knights[j].isQueenFound(xQueen, yQueen);
                        knights[j].coincideWithKnight(knights, j, numberOfKnights);
                        writer.println("No Exception");

                    } catch (NotACoordinateException e) {
                        writer.println(e.getMessage());
                    } catch (QueenFoundException e) {
                        writer.println(e.getMessage());
                        flag = 1;
                        break;

                    } catch (StackEmptyException e) {
                        writer.println(e.getMessage());
                    } catch (OverlapException e) {
                        writer.println(e.getMessage());
                    }
                }

            }
            if(flag ==1)
                break;
        }

        writer.close();

    }
}



class Coordinate{
    int x , y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

class Knight implements Comparable<Knight>{
    String name;
    int xKnight, yKnight;
    boolean queenFound;
    boolean inGrid;

    public boolean isInGrid() {
        return inGrid;
    }

    public void setInGrid(boolean inGrid) {
        this.inGrid = inGrid;
    }

    Stack<Object> st = new Stack<>();



    public boolean getQueenFound() {
        return queenFound;
    }

    public void setQueenFound(boolean queenFound) {
        this.queenFound = queenFound;
    }

    public Stack<Object> getSt() {
        return st;
    }

    public void setSt(Stack<Object> st) {
        this.st = st;
    }

    public Knight(String name, int xKnight, int yKnight, boolean queenFound, boolean inGrid) {
        this.name = name;
        this.xKnight = xKnight;
        this.yKnight = yKnight;
        this.queenFound = queenFound;
        this.inGrid = inGrid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getxKnight() {
        return xKnight;
    }

    public void setxKnight(int xKnight) {
        this.xKnight = xKnight;
    }

    public int getyKnight() {
        return yKnight;
    }

    public void setyKnight(int yKnight) {
        this.yKnight = yKnight;
    }

    @Override
    public int compareTo(Knight o) {
        //System.out.println("chal raha hu");
        return this.getName().compareTo(o.getName());
    }


    Object remove() throws NotACoordinateException, StackEmptyException {

        if(this.getSt().size()==0){
            this.setInGrid(false);
            throw new StackEmptyException("Stack Empty Exception : Stack Empty Exception");
        }
        Object o = this.getSt().pop();
        if(o instanceof Coordinate){
            return o;
        }
        else

        {
            throw new NotACoordinateException("NonCoordinateException : Not a Coordinate Exception " + o);

        }

    }

    void isQueenFound(int xQueen, int yQueen) throws QueenFoundException {
        if(this.getxKnight() == xQueen && this.getyKnight()==yQueen){
            this.setQueenFound(true);
            throw new QueenFoundException("Queen Found Exception: Queen has been found. Abort!");

        }
    }

    void coincideWithKnight(Knight[] knights, int j, int numberOfKnights) throws OverlapException {

        for(int i=0;i<numberOfKnights;i++){
            if(i!=j){
                if(this.getxKnight()==knights[i].getxKnight() && this.getyKnight()==knights[i].getyKnight()){
                    knights[i].setInGrid(false);
                    throw new OverlapException("Overlap Exception : Knights Overlap Exception" + knights[i].getName());
                }
            }
        }
    }
}

class OverlapException extends Exception{
    public OverlapException(String message) {
        super(message);
    }
}

class NotACoordinateException extends Exception{
    public NotACoordinateException(String message) {
        super(message);
    }
}

class QueenFoundException extends Exception{
    public QueenFoundException(String message) {
        super(message);
    }
}

class StackEmptyException extends Exception{
    public StackEmptyException(String message) {
        super(message);
    }
}

class Read {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                    reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }

    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }

    static float nextFloat() throws IOException {
        return Float.parseFloat(next());
    }
}
