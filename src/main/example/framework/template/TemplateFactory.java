package example.framework.template;

public interface TemplateFactory {

    Template create(String templateName);

    Template create(String groupName, String templateName);
}
