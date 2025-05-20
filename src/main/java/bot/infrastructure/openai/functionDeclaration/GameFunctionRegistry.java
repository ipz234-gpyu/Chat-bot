package bot.infrastructure.openai.functionDeclaration;

import com.google.genai.types.FunctionDeclaration;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class GameFunctionRegistry {
    private static final Map<String, Method> methods = new HashMap<>();

    private GameFunctionRegistry() {
        try {
            methods.put(
                    CharacterReaction.FUNCTION_NAME,
                    CharacterReaction.class.getMethod(
                            CharacterReaction.FUNCTION_NAME
                    )
            );
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /** Функції, які передаємо в LLM як декларації */
    public static ImmutableList<FunctionDeclaration> all() {
        return ImmutableList.of(
                CharacterReaction.DECLARATION,
                DataFinalization.DECLARATION
        );
    }

    /** Всі зареєстровані Method-об’єкти */
    public static ImmutableList<Method> getAllMethods() {
        return ImmutableList.copyOf(methods.values());
    }

    /** Отримати конкретний Method по імені функції */
    public static Method getMethod(String name) {
        return methods.get(name);
    }
}