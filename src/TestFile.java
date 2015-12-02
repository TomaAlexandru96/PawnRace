import java.io.*;

public class TestFile {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("aa.txt"));
        String x;
        x = br.readLine();
        while (x != null) {
            //System.out.println(x);
            x = br.readLine();
        }
        System.out.println("done..");
    }
}