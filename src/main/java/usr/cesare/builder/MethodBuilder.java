package usr.cesare.builder;


import org.mybatis.generator.api.dom.java.*;

import java.util.Arrays;

public class MethodBuilder extends AbstractBuilder<Method> {
    public static MethodBuilder create(){
        MethodBuilder builder = new MethodBuilder();
        builder.t = new Method();
        builder.t.setVisibility(JavaVisibility.PUBLIC);
        builder.t.setReturnType(null);
        builder.t.setName("method");
        return builder;
    }

    public MethodBuilder withVisibility(JavaVisibility visibility){
        t.setVisibility(visibility);
        return this;
    }

    public MethodBuilder withReturnType(FullyQualifiedJavaType returnType){
        t.setReturnType(returnType);
        return this;
    }

    public MethodBuilder withName(String name){
        t.setName(name);
        return this;
    }

    public MethodBuilder withParameters(Parameter... parameters){
        if(parameters != null){
            for(Parameter parameter : parameters){
                t.addParameter(parameter);
            }
        }
        return this;
    }
    public MethodBuilder withParameter(FullyQualifiedJavaType type, String name){
        t.addParameter(new Parameter(type, name));
        return this;
    }
    public MethodBuilder withParameter(Parameter parameter){
        t.addParameter(parameter);
        return this;
    }

    public MethodBuilder withBodyLine(String line){
        t.addBodyLine(line);
        return this;
    }

    public MethodBuilder withBodyLines(String... line){
        t.addBodyLines(Arrays.asList(line));
        return this;
    }
}