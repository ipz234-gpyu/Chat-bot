package bot.infrastructure.openai.functionDeclaration;

import com.google.common.collect.ImmutableList;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;

import java.util.HashMap;
import java.util.Map;

public class FunctionExemple {
    private static final String FUNCTION_NAME = "functionName";
    private static final String FUNCTION_DESCRIPTION = "functionDescription";
    private static final String STRING_PARAM_NAME = "stringParam";
    private static final String INTEGER_PARAM_NAME = "integerParam";
    private static final String DOUBLE_PARAM_NAME = "doubleParam";
    private static final String FLOAT_PARAM_NAME = "floatParam";
    private static final String BOOLEAN_PARAM_NAME = "booleanParam";

    private static final ImmutableList<String> REQUIRED_PARAM_NAMES =
            ImmutableList.of(
                    STRING_PARAM_NAME,
                    INTEGER_PARAM_NAME,
                    DOUBLE_PARAM_NAME,
                    FLOAT_PARAM_NAME,
                    BOOLEAN_PARAM_NAME);

    private static final FunctionDeclaration EXPECTED_FUNCTION_DECLARATION =
            FunctionDeclaration.builder()
                    .name(FUNCTION_NAME)
                    .description(FUNCTION_DESCRIPTION)
                    .parameters(
                            Schema.builder()
                                    .type(Type.Known.OBJECT)
                                    .properties(buildPropertiesMap())
                                    .required(REQUIRED_PARAM_NAMES)
                                    .build())
                    .build();

    private static Map<String, Schema> buildPropertiesMap() {
        Map<String, Schema> properties = new HashMap<>();
        properties.put(
                STRING_PARAM_NAME,
                Schema.builder().type(Type.Known.STRING).title(STRING_PARAM_NAME).build());
        properties.put(
                INTEGER_PARAM_NAME,
                Schema.builder().type(Type.Known.INTEGER).title(INTEGER_PARAM_NAME).build());
        properties.put(
                DOUBLE_PARAM_NAME,
                Schema.builder().type(Type.Known.NUMBER).title(DOUBLE_PARAM_NAME).build());
        properties.put(
                FLOAT_PARAM_NAME, Schema.builder().type(Type.Known.NUMBER).title(FLOAT_PARAM_NAME).build());
        properties.put(
                BOOLEAN_PARAM_NAME,
                Schema.builder().type(Type.Known.BOOLEAN).title(BOOLEAN_PARAM_NAME).build());
        return properties;
    }
}
