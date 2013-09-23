package FBS;

import Utils.Combination;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkornilova
 * Date: 12.09.13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class GetAllFileCombinations {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        List<List<String>> pairs = new ArrayList<List<String>>();
        List<String> in = new ArrayList<String>();
        in.add("01");
        in.add("02");
        in.add("03");
        in.add("11");
        in.add("12");
        in.add("13");
        in.add("21");
        in.add("22");
        in.add("23");
        Combination<String> userPermutations = new Combination<String>(in, 3);
        for (; userPermutations.hasNext(); ) {
            pairs.add( userPermutations.next() );
        }

        for (List<String> pair : pairs) {
            System.out.print(pair.get(0)+" " +
                    pair.get(1)+" " +
                    pair.get(2)+"\n");
        }

        System.out.print("Count combinations:" + pairs.size());
    }
}
