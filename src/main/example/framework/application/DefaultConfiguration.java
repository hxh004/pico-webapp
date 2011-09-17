package example.framework.application;

import example.framework.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class DefaultConfiguration implements Configuration {

    private final StrSubstitutor substitutor;
    private final Map<String, String> properties;

    public DefaultConfiguration(Map<String, String> properties) {
        Map tokens = createTokensToSubstitute(properties);
        this.substitutor = new StrSubstitutor(tokens);
        this.properties = properties;
    }

    private static Map createTokensToSubstitute(Map<String, String> properties) {
        Map tokens = new HashMap(System.getProperties());
        tokens.putAll(properties);
        return tokens;
    }

    public String get(String key) {
        String value = properties.get(key);
        return StringUtils.isNotBlank(value) ? substitutor.replace(value) : value;
    }
}
