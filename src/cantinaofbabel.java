import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
public class cantinaofbabel{
    public static ArrayList<String> languages = new ArrayList<>();

    public static ArrayList<ArrayList<Integer>> scc;
    public static int[] lowLink;
    public static boolean[] visited;
    public static ArrayList<Integer>[] paths;
    public static Stack<Integer> stack;

    public static void tarjans(int current, int time){
        lowLink[current] = time;
        visited[current] = true;
        stack.push(current);

        for(int i = 0;i < paths[current].size();i++){
            int next = paths[current].get(i);
            if(!visited[next]) tarjans(paths[current].get(i), time + 1);
            if(lowLink[next] < lowLink[current]) lowLink[current] = lowLink[next];
        }

        if(lowLink[current] == time){
            ArrayList<Integer> group = new ArrayList<>();
            while(true){
                Integer node = stack.pop();
                group.add(node);
                lowLink[node] = Integer.MAX_VALUE;
                if(node == current) break;
            }
            scc.add(group);
        }
    }

    public static int language(String lang){
        int index = languages.indexOf(lang);
        if(index < 0){
            index = languages.size();
            languages.add(lang);
        }
        return index;
    }

    public static void main(String[] args) throws IOException{
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        int n = in.nextInt();
        String[] names = new String[n];
        int[] speaks = new int[n];
        ArrayList<Integer>[] understands = new ArrayList[n];
        for(int i = 0;i < n;i++){
            names[i] = in.next();
            speaks[i] = language(in.next());

            understands[i] = new ArrayList<>();
            understands[i].add(speaks[i]);
            while(!in.lineEnd()) understands[i].add(language(in.next()));
        }

        ArrayList<Integer>[] speakers = new ArrayList[languages.size()];
        for(int i = 0;i < speakers.length;i++) speakers[i] = new ArrayList<>();
        for(int i = 0;i < n;i++) speakers[speaks[i]].add(i);

        paths = new ArrayList[n];
        for(int i = 0;i < n;i++) paths[i] = new ArrayList<>();
        for(int i = 0;i < n;i++){
            for(int j = 0;j < understands[i].size();j++){
                paths[i].addAll(speakers[understands[i].get(j)]);
            }
        }


        scc = new ArrayList<>();
        lowLink = new int[n];
        visited = new boolean[n];
        stack = new Stack<>();

        for(int i = 0;i < n;i++) if(!visited[i]) tarjans(i, 0);

        int min = Integer.MAX_VALUE;
        for(int i = 0;i < scc.size();i++) min = Math.min(min, n - scc.get(i).size());

        out.println(min);
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
