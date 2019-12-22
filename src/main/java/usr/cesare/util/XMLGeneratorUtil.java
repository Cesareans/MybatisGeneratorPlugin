package usr.cesare.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import usr.cesare.builder.XmlElementBuilder;
import usr.cesare.comparator.XMLElementComparator;

import java.util.List;

public class XMLGeneratorUtil {
    public static Element getBaseColumnListElement(IntrospectedTable introspectedTable) {
        return XmlElementBuilder.create("include")
                .withAttribute("refid", introspectedTable.getBaseColumnListId())
                .build();
    }

    public static String whereEqualStatement(IntrospectedColumn column){
        return column.getActualColumnName() + " = " + "#{" + column.getJavaProperty() + ",jdbcType=" + column.getJdbcTypeName() + "}";
    }

    public static String getXmlElementId(XmlElement element){
        List<Attribute> attributes = element.getAttributes();
        for(Attribute attribute : attributes){
            if(attribute.getName().equals("id")){
                return attribute.getValue();
            }
        }
        return null;
    }

    public static void insertElementProperly(XmlElement element, List<Element> elements) {
        for (int i = 0; i < elements.size(); i++) {
            if(elements.get(i) instanceof XmlElement){
                XmlElement elementInPlace = ((XmlElement) elements.get(i));
                if(XMLElementComparator.comparator.compare(elementInPlace, element) > 0){
                    elements.add(i, element);
                    return;
                }
            }
        }
        elements.add(element);
    }
}
