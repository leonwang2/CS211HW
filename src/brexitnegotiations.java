import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
public class brexitnegotiations{
    public static class Topic{
        public int length;
        public int depLength;
        public int inDegree;
        public int[] dependencies;
        public ArrayList<Topic> next = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException{
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        int n = in.nextInt();
        Topic[] topics = new Topic[n];
        for(int i = 0;i < n;i++){
            topics[i] = new Topic();
            topics[i].length = in.nextInt();
            topics[i].inDegree = in.nextInt();
            topics[i].dependencies = new int[topics[i].inDegree];
            for(int j = 0;j < topics[i].inDegree;j++) topics[i].dependencies[j] = in.nextInt() - 1;
        }

        for(int i = 0;i < n;i++){
            for(int j = 0;j < topics[i].dependencies.length;j++){
                topics[topics[i].dependencies[j]].next.add(topics[i]);
            }
        }

        int index = 0;
        Topic[] order = new Topic[n];
        LinkedList<Topic> queue = new LinkedList<Topic>();
        for(int i = 0;i < n;i++) if(topics[i].inDegree == 0) queue.add(topics[i]);
        while(!queue.isEmpty()){
            Topic current = queue.pop();
            order[index++] = current;

            for(Topic next : current.next){
                next.inDegree--;
                if(next.inDegree == 0) queue.add(next);
            }
        }

        for(int i = n - 1;i >= 0;i--){
            Topic current = order[i];
            current.depLength = current.length;
            for(Topic next : current.next) current.depLength += next.depLength;
        }

        Arrays.sort(order, Comparator.comparingInt(t -> -t.depLength));
        int max = 0;
        for(int i = 0;i < n;i++) max = Math.max(max, i + order[i].length);
//        int max = 0, finished = 0;
//        PriorityQueue<Topic> queue = new PriorityQueue<>(Comparator.comparingInt(t -> -t.length));
//        for(int i = 0;i < n;i++) if(topics[i].inDegree == 0) queue.add(topics[i]);
//        while(!queue.isEmpty()){
//            Topic current = queue.poll();
//            max = Math.max(max, (finished++) + current.length);
//
//            for(Topic next : current.next){
//                next.inDegree--;
//                if(next.inDegree == 0) queue.add(next);
//            }
//        }
        out.println(max);
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
