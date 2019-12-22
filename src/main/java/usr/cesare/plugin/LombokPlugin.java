package usr.cesare.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;

public class LombokPlugin extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }
    private String[] annotations;
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if(properties.getProperty("annotations") != null) {
            annotations = properties.getProperty("annotations").split(",");
        }
    }
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (String annotation : annotations) {
            topLevelClass.addImportedType("lombok." + annotation);
            topLevelClass.addAnnotation("@" + annotation);
        }
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }
}
