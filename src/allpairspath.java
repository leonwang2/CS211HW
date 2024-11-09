import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
//GenAI Chat Link: https://chatgpt.com/share/67243377-e708-8001-a16f-59155467baf8 (I had it help fix my code)
public class allpairspath{
    public static void main(String[] args) throws IOException{
        int inf = Integer.MAX_VALUE / 2;

        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        while(true){
            int n = in.nextInt(), m = in.nextInt(), q = in.nextInt();
            if(n == 0 && m == 0 && q == 0) break;

            int[][] dp = new int[n][n];
            for(int i = 0;i < n;i++){
                Arrays.fill(dp[i], inf);
                dp[i][i] = 0;
            }
            for(int i = 0;i < m;i++){
                int a = in.nextInt(), b = in.nextInt();
                dp[a][b] = Math.min(dp[a][b], in.nextInt());
            }

            for(int k = 0;k < n;k++){
                for(int i = 0;i < n;i++){
                    for(int j = 0;j < n;j++){
                        if(dp[i][k] < inf && dp[k][j] < inf) dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j]);
                    }
                }
            }

            //It seems like I messed up my negative cycle detection
            for(int k = 0;k < n;k++){
                if(dp[k][k] < 0){
                    for(int i = 0;i < n;i++){
                        for(int j = 0;j < n;j++){
                            if(dp[i][k] < inf && dp[k][j] < inf){
                                dp[i][j] = -inf;  // Mark as part of negative cycle path
                            }
                        }
                    }
                }
            }

            for(int i = 0;i < q;i++){
                int a = in.nextInt(), b = in.nextInt();
                if(dp[a][b] >= inf) out.println("Impossible");
                else if(dp[a][b] == -inf) out.println("-Infinity");
                else out.println(dp[a][b]);
            }
            out.println();
        }

        in.close();
        out.close();
    }


    //Below is what GenAI wrote (I only had it generate an answer as a last resort)

//    static final int INF = Integer.MAX_VALUE / 2; // Avoid overflow in addition
//
//    public static void main(String[] args) throws IOException{
//        FastReader scanner = new FastReader();
//        StringBuilder output = new StringBuilder();
//
//        while(true){
//            // Read n, m, q
//            int n = scanner.nextInt();
//            int m = scanner.nextInt();
//            int q = scanner.nextInt();
//            if(n == 0 && m == 0 && q == 0) break;
//
//            // Initialize distance matrix
//            int[][] dist = new int[n][n];
//            for(int i = 0;i < n;i++){
//                Arrays.fill(dist[i], INF);
//                dist[i][i] = 0;
//            }
//
//            // Read edges
//            for(int i = 0;i < m;i++){
//                int u = scanner.nextInt();
//                int v = scanner.nextInt();
//                int w = scanner.nextInt();
//                dist[u][v] = Math.min(dist[u][v], w);
//            }
//
//            // Run Floyd-Warshall algorithm
//            for(int k = 0;k < n;k++){
//                for(int i = 0;i < n;i++){
//                    for(int j = 0;j < n;j++){
//                        if(dist[i][k] < INF && dist[k][j] < INF){
//                            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
//                        }
//                    }
//                }
//            }
//
//            // Detect nodes affected by negative cycles
//            for(int k = 0;k < n;k++){
//                if(dist[k][k] < 0){  // Negative cycle found
//                    for(int i = 0;i < n;i++){
//                        for(int j = 0;j < n;j++){
//                            if(dist[i][k] < INF && dist[k][j] < INF){
//                                dist[i][j] = -INF;  // Mark as part of negative cycle path
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Process queries
//            for(int i = 0;i < q;i++){
//                int u = scanner.nextInt();
//                int v = scanner.nextInt();
//                if(dist[u][v] == INF){
//                    output.append("Impossible\n");
//                }else if(dist[u][v] == -INF){
//                    output.append("-Infinity\n");
//                }else{
//                    output.append(dist[u][v]).append("\n");
//                }
//            }
//            output.append("\n");  // Blank line after each test case
//        }
//
//        System.out.print(output);
//    }


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
            while(c <= ' ') c = read();
            boolean neg = (c == '-');
            if(neg) c = read();
            do{
                ret = ret * 10 + c - '0';
            }while((c = read()) >= '0' && c <= '9');
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
