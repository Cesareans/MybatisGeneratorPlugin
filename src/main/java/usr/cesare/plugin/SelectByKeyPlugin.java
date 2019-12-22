package usr.cesare.plugin;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import usr.cesare.builder.MethodBuilder;
import usr.cesare.builder.XmlElementBuilder;
import usr.cesare.comparator.XMLElementComparator;
import usr.cesare.util.JavaGeneratorUtil;
import usr.cesare.util.XMLGeneratorUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectByKeyPlugin extends PluginAdapter {
    private CommentGenerator commentGenerator;
    private List<IntrospectedColumn> keyColumns;

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String keyColumnsProperty = introspectedTable.getTableConfiguration().getProperty("keyColumns");
        if(keyColumnsProperty != null) {
            List<String> keyColumnNames = Arrays.asList(keyColumnsProperty.split(","));
            keyColumns = introspectedTable.getAllColumns().stream()
                    .filter(column -> keyColumnNames.contains(column.getJavaProperty()))
                    .collect(Collectors.toList());
        }else{
            keyColumns = Collections.emptyList();
        }
        commentGenerator = context.getCommentGenerator();
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,IntrospectedTable introspectedTable) {
        for (IntrospectedColumn column : keyColumns){
            Method selectKeyMethod = MethodBuilder.create()
                    .withVisibility(JavaVisibility.DEFAULT)
                    .withReturnType(JavaGeneratorUtil.getModelType(introspectedTable))
                    .withName(JavaGeneratorUtil.selectWithoutBlobName(column.getJavaProperty()))
                    .withParameter(column.getFullyQualifiedJavaType(), column.getJavaProperty())
                    .build();
            JavaGeneratorUtil.insertMethodProperly(selectKeyMethod, interfaze.getMethods());
            commentGenerator.addGeneralMethodComment(selectKeyMethod, introspectedTable);
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        for (IntrospectedColumn column : keyColumns){
            XmlElement selectElement = XmlElementBuilder.create("select")
                    .withAttribute("id", JavaGeneratorUtil.selectWithoutBlobName(column.getJavaProperty()))
                    .withAttribute("resultMap", introspectedTable.getBaseResultMapId())
                    .withAttribute("parameterType", column.getFullyQualifiedJavaType().getFullyQualifiedName())
                    .withTextElement("select")
                    .withElement(XMLGeneratorUtil.getBaseColumnListElement(introspectedTable))
                    .withTextElement("from " + introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())
                    .withTextElement("where " + XMLGeneratorUtil.whereEqualStatement(column))
                    .build();
            XMLGeneratorUtil.insertElementProperly(selectElement, rootElement.getElements());

            commentGenerator.addComment(rootElement);
        }

        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(rootElement, introspectedTable);
    }


    public boolean validate(List<String> list) {
        return true;
    }


}
