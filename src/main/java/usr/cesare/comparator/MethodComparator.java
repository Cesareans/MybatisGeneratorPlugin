package usr.cesare.comparator;

import org.mybatis.generator.api.dom.java.Method;

import java.util.Comparator;
import java.util.Objects;

public class MethodComparator implements Comparator<Method> {
    @Override
    public int compare(Method method1, Method method2) {
        return StringComparator.comparator.compare(method1.getName(), method2.getName());
    }
}
