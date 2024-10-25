import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
public class rockclimbing{
    public static int[] d4x = {1, 0, -1, 0};
    public static int[] d4y = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException{
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        int h = in.nextInt(), w = in.nextInt();
        int[][] energy = new int[w][h + 2];
        for(int i = 0;i < w;i++) in.next();
        for(int i = h - 1;i >= 0;i--) for(int j = 0;j < w;j++) energy[j][i + 1] = in.nextInt();

        int max = 9 * 15;
        int min = 0;
        while(min < max){
            int mid = min + (max - min) / 2;

            boolean works = false;

            LinkedList<Integer> queue = new LinkedList<>();
            int[][] maxs = new int[w][h + 2];
            for(int i = 0;i < w;i++){
                Arrays.fill(maxs[i], 0);
                queue.add(mid * 65536 + i * 256);
                maxs[i][0] = mid;
            }

            while(!queue.isEmpty()){
                int encoded = queue.pop();
                int e = encoded / 65536;
                int x = (encoded % 65536) / 256;
                int y = encoded % 256;

                if(e < maxs[x][y]) continue;
                if(y == h + 1){
                    works = true;
                    break;
                }

                maxs[x][y] = e;
                for(int i = 0;i < 4;i++){
                    int nextX = x + d4x[i];
                    int nextY = y + d4y[i];
                    if(nextX < 0 || nextX >= w || nextY < 0 || nextY >= h + 2) continue;
                    int nextE = e - energy[nextX][nextY];

                    if(nextE >= 0 && nextE <= 9 * 16 && nextE > maxs[nextX][nextY]){
                        queue.add(nextE * 65536 + nextX * 256 + nextY);
                        maxs[nextX][nextY] = nextE;
                    }
                }
            }

            if(works) max = mid;
            else min = mid + 1;
        }

        out.println(Math.max(min - 1, 0));
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
