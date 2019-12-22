package usr.cesare.plugin;


import org.mybatis.generator.api.PluginAdapter;


import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import usr.cesare.builder.FieldBuilder;
import usr.cesare.builder.MethodBuilder;
import usr.cesare.builder.XmlElementBuilder;

public class PaginationPlugin extends PluginAdapter {
    private boolean pageFromZero = false;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (properties.getProperty("pageFromZero") != null) {
            pageFromZero = Boolean.getBoolean(properties.getProperty("pageFromZero"));
        }
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // add field, getter, setter for limit clause
        addLimit(topLevelClass, introspectedTable, "page");
        addLimit(topLevelClass, introspectedTable, "rows");

        CommentGenerator commentGenerator = context.getCommentGenerator();
        Method method = MethodBuilder.create()
                .withVisibility(JavaVisibility.PUBLIC)
                .withReturnType(PrimitiveTypeWrapper.getIntegerInstance())
                .withName("getOffset")
                .withBodyLine(pageFromZero ? "return page * rows;" : "return (page - 1) * rows;")
                .build();
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        element.addElement(
                XmlElementBuilder.create("if")
                        .withAttribute("test", "offset >= 0 and rows >= 0")
                        .withTextElement("limit #{offset}, #{rows}")
                        .build()
        );
        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    private void addLimit(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name) {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);

        Field field = FieldBuilder.create().withVisibility(JavaVisibility.PROTECTED).withType(PrimitiveTypeWrapper.getIntegerInstance()).withName(name).build();
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        Method method = MethodBuilder.create()
                .withName("set" + camel)
                .withParameter(new Parameter(PrimitiveTypeWrapper.getIntegerInstance(), name))
                .withBodyLine("this." + name + "=" + name + ";")
                .build();
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);

        method = MethodBuilder.create()
                .withReturnType(PrimitiveTypeWrapper.getIntegerInstance())
                .withName("get" + camel)
                .withBodyLine("return " + name + ";")
                .build();
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }

    /**
     * This plugin is always valid - no properties are required
     */
    public boolean validate(List<String> warnings) {
        return true;
    }

}

