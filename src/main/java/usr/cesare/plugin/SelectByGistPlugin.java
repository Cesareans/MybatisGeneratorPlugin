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
import usr.cesare.logger.LineLogger;
import usr.cesare.util.JavaGeneratorUtil;
import usr.cesare.util.PropertiesName;
import usr.cesare.util.XMLGeneratorUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectByGistPlugin extends PluginAdapter {
    private CommentGenerator commentGenerator;
    private List<IntrospectedColumn> gistColumns;

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        String gistColumnsProperty = introspectedTable.getTableConfiguration().getProperty(PropertiesName.GistColumns);
        if(gistColumnsProperty != null) {
            List<String> gistColumnNames = Arrays.asList(gistColumnsProperty.split(","));
            gistColumns = introspectedTable.getAllColumns().stream()
                    .filter(column -> gistColumnNames.contains(column.getJavaProperty()))
                    .collect(Collectors.toList());
        }else{
            gistColumns = Collections.emptyList();
        }
        commentGenerator = context.getCommentGenerator();
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (IntrospectedColumn column : gistColumns){
            Method selectKeyMethod = MethodBuilder.create()
                    .withVisibility(JavaVisibility.DEFAULT)
                    .withReturnType(JavaGeneratorUtil.getModelListType(introspectedTable))
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
        for (IntrospectedColumn column : gistColumns){
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
