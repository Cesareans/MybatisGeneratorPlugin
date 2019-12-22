package usr.cesare.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import usr.cesare.logger.LineLogger;

import java.util.List;
import java.util.Properties;

import static usr.cesare.util.PropertiesName.Annotation;

public class FieldAnnotationPlugin extends PluginAdapter {
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        LineLogger.log(field.getName());
        String annotationProperty = introspectedColumn.getProperties().getProperty(Annotation);
        if(annotationProperty != null) {
            String[] annotations = annotationProperty.split(",");
            for (String annotation : annotations) {
                topLevelClass.addImportedType(annotation);
                field.addAnnotation("@" + annotation.substring(annotation.lastIndexOf(".") + 1));
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }
}
