package usr.cesare.util;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.lang.reflect.Array;
import java.util.*;


public class JavaGeneratorUtil {
    public static String selectWithoutBlobName(String target) {
        return "selectBy" + Character.toUpperCase(target.charAt(0)) + target.substring(1);
    }

    public static FullyQualifiedJavaType getModelType(IntrospectedTable table) {
        return table.getRules().generateRecordWithBLOBsClass() ?
                new FullyQualifiedJavaType(table.getRecordWithBLOBsType()) :
                new FullyQualifiedJavaType(table.getBaseRecordType());
    }

    public static FullyQualifiedJavaType getModelListType(IntrospectedTable table){
        return table.getRules().generateRecordWithBLOBsClass() ?
                new FullyQualifiedJavaType("List<" + table.getRecordWithBLOBsType() + ">") :
                new FullyQualifiedJavaType("List<" + table.getBaseRecordType() + ">");
    }

    public static void insertMethodProperly(Method method, List<Method> methods) {
        for (int i = 0; i < methods.size(); i++) {
            if(methods.get(i).getName().compareTo(method.getName()) > 0){
                methods.add(i, method);
                return;
            }
        }
        methods.add(method);
    }
}
