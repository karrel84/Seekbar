package karrel.com.seekbar;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void calPosition() throws Exception {
        int input = 3;
        System.out.println(String.format("input : %s, output : %s", input, calPosition(input)));
    }

    private int calPosition(int point) {
//        System.out.print("point : " + point);
        int[] gap = new int[]{180, 360, 540, 720, 900};

        for (int i = gap.length - 1; i >= 0; i--) {
            if (gap[i] < point) {
                System.out.println(gap[i]);
                int value = 0;
                if (gap[i] + 90 < point) {
                    value = gap[i] + 180;
                } else {
                    value = gap[i];
                }
                return value;
            }

        }
        return 0;
    }
}