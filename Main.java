import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int[] num1 = {1, 2};
        int[] num2 = {3, 4};
        double answer = findMedianSortedArrays(num1, num2);
        System.out.println(answer);
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        List<Integer> number = new ArrayList<>();
        for (int x : nums1) {
            number.add(x);
        }
        for (int x : nums2) {
            number.add(x);
        }
        Collections.sort(number);
        if (number.size() % 2 == 1) {
            return number.get(number.size() / 2);
        } else {
            return (number.get(number.size() / 2 - 1) + number.get(number.size() / 2)) / 2.0;
        }
    }
}
