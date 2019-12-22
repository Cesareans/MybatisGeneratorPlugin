package usr.cesare.comparator;

import java.util.Comparator;
import java.util.Objects;

public class StringComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        if(s1==null && s2!=null) return -1;
        if(s1!=null && s2==null) return 1;
        return Objects.compare(s1, s2, String::compareTo);
    }

    public static StringComparator comparator = new StringComparator();
}
