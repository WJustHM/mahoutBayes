package canopy;

import java.util.ArrayList;
import java.util.List;

public class cantest {
    public static void main(String[] args) {
        li();
    }

    public static void li() {
        List<Double> l = new ArrayList<Double>();
        for (int i = 0; i < 100; i++) {
            l.add(Math.random() * 100);
        }
        int T1 = 10;
        int T2 = 5;
        double dian = 15;
        int j = 1;
        double next = 0;
        int num = 0;
        while (true) {

            if (l.size() == 0)
                break;
            num++;
            List<Double> l1 = new ArrayList<Double>();
            if (j == 1) {
                for (int i = 0; i < l.size(); i++) {
                    if (Math.abs(l.get(i) - dian) < T2) {
                        l1.add(l.get(i));
                        l.remove(i);
                    }
                }
                j++;
            } else {
                next = l.get((int) Math.random() * l.size());
                System.out.println("qqqqqqqqqqqq"+next);
                for (int i = 0; i < l.size(); i++) {
                    if (Math.abs(l.get(i) - next) < T2) {
                        l1.add(l.get(i));
                        l.remove(i);
                    }
                }
            }
            System.out.println("----+++++++++" + l1);

        }
        System.out.println("--------" + l.size() + "num" + num);
    }
}
