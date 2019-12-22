package usr.cesare.builder;


import org.mybatis.generator.api.dom.java.*;

public class FieldBuilder extends AbstractBuilder<Field> {

    public static FieldBuilder create(){
        FieldBuilder fieldBuilder = new FieldBuilder();
        fieldBuilder.t = new Field();
        fieldBuilder.t.setVisibility(JavaVisibility.PRIVATE);
        fieldBuilder.t.setName("field");
        return fieldBuilder;
    }

    public FieldBuilder withVisibility(JavaVisibility visibility){
        t.setVisibility(visibility);
        return this;
    }
    public FieldBuilder withType(FullyQualifiedJavaType type){
        t.setType(type);
        return this;
    }
    public FieldBuilder withName(String name){
        t.setName(name);
        return this;
    }
}
