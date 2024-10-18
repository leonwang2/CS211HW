import java.io.*;
import java.util.*;

//Author: Leon Wang
//It is ok to share my code anonymously for educational purposes
public class committeeassignment{
    public static void main(String[] args) throws IOException{
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);

        //Precalculate powers of 2 for future bit manipulation
        int[] pows = new int[15];
        pows[0] = 1;
        for(int i = 1;i < 15;i++) pows[i] = pows[i - 1] * 2;

        //Loop through all test cases
        while(true){
            //Read in inputs
            int n = in.nextInt(), m = in.nextInt();

            //If there are no people, exit the loop
            if(n == 0) break;

            //Keep an 2D array of who hates each other where if hate[i][j], person i hates person j
            boolean[][] hate = new boolean[15][15];

            //Keep track of unique names
            ArrayList<String> names = new ArrayList<>();

            //Loop through mutual haters
            for(int i = 0;i < m;i++){
                //Read in names
                String a = in.next(), b = in.next();

                //Assign index to the first name
                //If the name has been mentioned before, give it that index
                int ia = names.indexOf(a);

                //Otherwise, give it a new index and append the name to the arraylist
                if(ia < 0){
                    ia = names.size();
                    names.add(a);
                }

                //Same for the second name
                int ib = names.indexOf(b);
                if(ib < 0){
                    ib = names.size();
                    names.add(b);
                }

                //Update the hate array
                hate[ia][ib] = hate[ib][ia] = true;
            }

            //Keep a priority queue for BFS sorted by number of departments
            //Each array in the queue are of the format {index of next person to add, committee 1, committee 2, etc. }
            PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(arr -> arr.length));

            //Add base state (index of next person to add is 0, no commitees yet)
            queue.add(new int[]{0});

            //Keep track of answers
            int ans = 0;
            while(!queue.isEmpty()){
                //Get array with least committees
                int[] option = queue.poll();

                //If the next person to add is over the amount of people, this amount of committees (length of array) is the answer
                if(option[0] >= n){
                    ans = option.length - 1;
                    break;
                }

                //Try to add the next person to each of the currently defined committees
                for(int i = 1;i < option.length;i++){
                    boolean hates = false;

                    //See if there is anyone the next person hates in the committee
                    for(int j = 0;j < n;j++){
                        //We are storing people in a committee through the bits in an integer
                        //The first bit is person 1, the second is person 2, etc.
                        //So you can see if a person is in a committee by checking if when the value & pows[j] > 0
                        if((option[i] & pows[j]) > 0 && hate[option[0]][j]){
                            hates = true;
                            break;
                        }
                    }

                    //If there isn't any hated person in the committee, add the person to the committee and update the queue
                    if(!hates){
                        //Create new state array
                        int[] arr = Arrays.copyOf(option, option.length);

                        //Set the bit corresponding to the person to add to 1
                        arr[i] = arr[i] | pows[option[0]];

                        //Update next person
                        arr[0]++;
                        queue.add(arr);
                    }
                }

                //Create a new committee just for the next person
                int[] bigger = Arrays.copyOf(option, option.length + 1);

                //Update next person
                bigger[0]++;

                //Set the bit corresponding to the person to add to 1
                bigger[bigger.length - 1] = pows[option[0]];
                queue.add(bigger);
            }

            out.println(ans);
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
