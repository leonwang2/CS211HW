import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
//GenAI Chat Link: https://chatgpt.com/share/670f14c6-ccd0-8001-bdc7-b3f0a6f677cc
public class wifi{
//    public static void main(String[] args) throws IOException{
//        FastReader in = new FastReader();
//        PrintWriter out = new PrintWriter(System.out);
//        int tests = in.nextInt();
//        for(int test = 0;test < tests;test++){
//            int n = in.nextInt(), m = in.nextInt();
//            int[] houses = new int[m];
//            for(int i = 0;i < m;i++) houses[i] = in.nextInt();
//
//            Arrays.sort(houses);
//
//            double min = 0, max = houses[m - 1];
//            for(int j = 0;j < 50;j++){
//                double mid = min + (max - min) / 2;
//
//                int used = 0;
//                double last = -mid - 1;
//                for(int i = 0;i < m && used < n + 1;i++){
//                    if(Math.abs(houses[i] - last) > mid){
//                        used++;
//                        last = houses[i] + mid;
//                    }
//                }
//
//                if(used <= n) max = mid;
//                else min = mid;
//            }
//
//            String value = Integer.toString((int)(min * 10 + 0.49));
//            if(value.length() == 1) out.print("0");
//            else out.print(value.substring(0, value.length() - 1));
//            out.print(".");
//            out.println(value.substring(value.length() - 1));
//        }
//        in.close();
//        out.close();
//    }

    //The above code is my original solution (without GenAI)

    //The code below is written with the help of GenAI
    public static boolean canPlaceAccessPoints(int[] houses, int n, double maxDist){
        //Greedily place access points
        int count = 1;  //First access point at the first house
        int lastPosition = houses[0];

        for(int i = 1;i < houses.length;i++){
            if(houses[i] - lastPosition > maxDist * 2){
                //Place a new access point here
                count++;
                lastPosition = houses[i];
                if(count > n) return false;
            }
        }
        return true;
    }

    public static double minMaxDistance(int[] houses, int n){
        Arrays.sort(houses);

        //Binary search over possible distances
        double low = 0, high = houses[houses.length - 1] - houses[0];

        //Perform a fixed number of iterations for precision
        for(int i = 0;i < 60;i++){
            double mid = (low + high) / 2;

            //If this mid value works, set maximum distance to it, otherwise set minimum distance to it
            if(canPlaceAccessPoints(houses, n, mid)) high = mid;
            else low = mid;
        }

        //Return result rounded to one decimal place
        return Math.round(high * 10.0) / 10.0;
    }

    public static void main(String[] args) throws IOException{
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int t = in.nextInt();  //Number of test cases
        for(int i = 0;i < t;i++){
            int n = in.nextInt();  //Number of access points
            int m = in.nextInt();  //Number of houses

            int[] houses = new int[m];
            for(int j = 0;j < m;j++) houses[j] = in.nextInt();

            double result = minMaxDistance(houses, n);
            out.printf("%.1f\n", result);
        }

        in.close();
        out.close();
    }

    /* FastReader code from Method 4 in the post https://www.geeksforgeeks.org/fast-io-in-java-in-competitive-programming/
       Modified nextLine() to allow arbitrary long lines,
       Modified fillBuffer(), read() to fix some issues
       Added next(), hasNext(), nextChar(), and lineEnd()
       Use nextInt(), nextLong(), or nextDouble() to read numbers
       Use nextChar() to read the next character which isn't a space
       Use next() to read a string.
       Use nextLine() to read in the next line that is not empty (i.e., it
           contains at least one character that is > 32 (' ').
    */
    static class FastReader{
        final public int BUFFER_SIZE = 1 << 16;
        public int MAX_LINE_SIZE = 1 << 16;
        public DataInputStream din;
        public byte[] buffer, lineBuf;
        public int bufferPointer, bytesRead;

        public FastReader(){
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            lineBuf = new byte[MAX_LINE_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public FastReader(String file_name) throws IOException{
            din = new DataInputStream(new FileInputStream(file_name));
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public boolean hasNext() throws IOException{
            byte c;
            while((c = read()) != -1){
                if(c > ' '){        // Find first byte bigger than ' '
                    bufferPointer--;
                    return true;
                }
            }
            return false;
        }

        public boolean lineEnd() throws IOException{
            byte c;
            if(bufferPointer > 0){
                bufferPointer--;
                if((c = read()) == '\n') return true;
            }

            while((c = read()) != -1){
                if(c > ' '){
                    bufferPointer--;
                    return false;
                }else if(c == '\n') return true;
            }

            bufferPointer++;
            return true;
        }

        // return the next line that contains at least one character > ' '
        public String nextLine() throws IOException{
            int ctr = 0;
            byte c;
            boolean empty = true;
            while((c = read()) != -1){
                if(c == '\r') continue;    // ignore '\r'
                if(c == '\n'){
                    if(empty){
                        ctr = 0;
                        continue;
                    } // read only spaces etc. until \n
                    else break;
                }
                if(ctr == MAX_LINE_SIZE){
                    MAX_LINE_SIZE *= 2;
                    lineBuf = Arrays.copyOf(lineBuf, MAX_LINE_SIZE);
                }
                lineBuf[ctr++] = c;
                if(c > ' ') empty = false;
            }
            return new String(lineBuf, 0, ctr);
        }

        public String next() throws IOException{
            int ctr = 0;
            byte c = read();
            while(c <= ' ') c = read();
            while(c > ' '){
                if(ctr == MAX_LINE_SIZE){
                    MAX_LINE_SIZE *= 2;
                    lineBuf = Arrays.copyOf(lineBuf, MAX_LINE_SIZE);
                }
                lineBuf[ctr++] = c;
                c = read();
            }
            return new String(lineBuf, 0, ctr);
        }

        public char nextChar() throws IOException{
            byte c = read();
            while(c <= ' ') c = read();
            return (char)c;
        }

        public int nextInt() throws IOException{
            int ret = 0;
            byte c = read();
            while(c <= ' ') c = read();
            boolean neg = (c == '-');
            if(neg) c = read();
            do{
                ret = ret * 10 + c - '0';
            }while((c = read()) >= '0' && c <= '9');

            if(neg) return -ret;
            return ret;
        }

        public long nextLong() throws IOException{
            long ret = 0;
            byte c = read();
            while(c <= ' ') c = read();
            boolean neg = (c == '-');
            if(neg) c = read();
            do{
                ret = ret * 10 + c - '0';
            }while((c = read()) >= '0' && c <= '9');
            if(neg) return -ret;
            return ret;
        }

        public double nextDouble() throws IOException{
            double ret = 0, div = 1;
            byte c = read();
            while(c <= ' ')
                c = read();
            boolean neg = (c == '-');
            if(neg) c = read();
            do{
                ret = ret * 10 + c - '0';
            }
            while((c = read()) >= '0' && c <= '9');
            if(c == '.'){
                while((c = read()) >= '0' && c <= '9'){
                    ret += (c - '0') / (div *= 10);
                }
            }
            if(neg) return -ret;
            return ret;
        }

        public void fillBuffer() throws IOException{
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
        }

        public byte read() throws IOException{
            if(bufferPointer == bytesRead) fillBuffer();
            if(bytesRead <= 0) return -1;  // No data
            return buffer[bufferPointer++];
        }

        public void close() throws IOException{
            if(din == null) return;
            din.close();
        }
    }
}
