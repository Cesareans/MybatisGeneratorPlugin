package usr.cesare.comparator;

import org.mybatis.generator.api.dom.xml.XmlElement;
import usr.cesare.util.XMLGeneratorUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class XMLElementComparator implements Comparator<XmlElement> {
    private HashMap<String, Integer> nodeHash = new HashMap<>();
    private XMLElementComparator(){
        nodeHash.put("resultMap", 0);
        nodeHash.put("sql", 1);
        nodeHash.put("select", 2);
        nodeHash.put("delete", 3);
        nodeHash.put("insert", 4);
        nodeHash.put("update", 5);
    }
    @Override
    public int compare(XmlElement xmlElement1, XmlElement xmlElement2) {
        if(xmlElement1.getName().equals(xmlElement2.getName())){
            String id1 = XMLGeneratorUtil.getXmlElementId(xmlElement1),
                    id2 = XMLGeneratorUtil.getXmlElementId(xmlElement2);
            return StringComparator.comparator.compare(id1, id2);
        }
        Integer nodePriority1 = nodeHash.getOrDefault(xmlElement1.getName(), Integer.MAX_VALUE);
        Integer nodePriority2 = nodeHash.getOrDefault(xmlElement2.getName(), Integer.MAX_VALUE);
        return nodePriority1 - nodePriority2;
    }

    public static XMLElementComparator comparator = new XMLElementComparator();
}
