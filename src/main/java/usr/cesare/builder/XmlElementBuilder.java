package usr.cesare.builder;

import org.mybatis.generator.api.dom.xml.*;

public class XmlElementBuilder extends AbstractBuilder<XmlElement> {

    public static XmlElementBuilder create(String node){
        XmlElementBuilder builder = new XmlElementBuilder();
        builder.t = new XmlElement(node);
        return builder;
    }

    public XmlElementBuilder withAttribute(String name, String value){
        t.addAttribute(new Attribute(name, value));
        return this;
    }

    public XmlElementBuilder withElement(Element e){
        t.addElement(e);
        return this;
    }

    public XmlElementBuilder withTextElement(String textElement){
        t.addElement(new TextElement(textElement));
        return this;
    }
}
