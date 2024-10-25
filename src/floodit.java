import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
public class floodit{
    public static int[] d4x = {1, 0, -1, 0};
    public static int[] d4y = {0, 1, 0, -1};

    public static void dfs(ArrayList<Integer> all, int[][] board, int current, int x, int y){
        if(x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] != current) return;

        board[x][y] = 7;
        all.add(x * 32 + y);
        for(int i = 0;i < 4;i++) dfs(all, board, current, x + d4x[i], y + d4y[i]);
        return;
    }

    public static int dfs2(int[][] board, boolean[][] visited, int current, int x, int y){
        if(x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] != current || visited[x][y]) return 0;

        visited[x][y] = true;
        int total = 1;
        for(int i = 0;i < 4;i++) total += dfs2(board, visited, current, x + d4x[i], y + d4y[i]);
        return total;
    }


    public static void main(String[] args) throws IOException{
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        int tests = in.nextInt();
        for(int t = 0;t < tests;t++){
            int n = in.nextInt();

            int[][] board = new int[n][n];
            for(int y = 0;y < n;y++){
                String str = in.nextLine();
                for(int x = 0;x < n;x++){
                    board[x][y] = str.charAt(x) - '1';
                }
            }
            int[] times = new int[6];

            int ans = 0;
            ArrayList<Integer> queue = new ArrayList<>();
            dfs(queue, board, board[0][0], 0, 0);
            while(queue.size() != n * n){
                int[] counts = new int[6];
                boolean[][] visited = new boolean[n][n];
                for(int i = 0;i < queue.size();i++){
                    int encoded = queue.get(i);
                    int x = encoded / 32;
                    int y = encoded % 32;

                    for(int j = 0;j < 4;j++){
                        int nx = x + d4x[j], ny = y + d4y[j];
                        if(nx < 0 || nx >= n || ny < 0 || ny >= n || board[nx][ny] == 7 || visited[nx][ny]) continue;
                        counts[board[nx][ny]] += dfs2(board, visited, board[nx][ny], nx, ny);
                    }
                }
                int max = 0;
                for(int j = 1;j < 6;j++) if(counts[j] > counts[max]) max = j;

                times[max]++;
                for(int i = 0;i < queue.size();i++){
                    int encoded = queue.get(i);
                    int x = encoded / 32;
                    int y = encoded % 32;

                    for(int j = 0;j < 4;j++){
                        int nx = x + d4x[j], ny = y + d4y[j];
                        if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                        if(board[nx][ny] == max){
                            board[nx][ny] = 7;
                            queue.add(nx * 32 + ny);
                        }
                    }
                }
                if(counts[max] > 0) ans++;
            }
            out.println(ans);
            out.print(times[0]);
            for(int i = 1;i < 6;i++) out.print(" " + times[i]);
            out.println();
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
