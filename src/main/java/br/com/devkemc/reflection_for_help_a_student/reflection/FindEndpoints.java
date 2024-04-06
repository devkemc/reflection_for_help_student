package br.com.devkemc.reflection_for_help_a_student.reflection;

import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.components.Controller;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Get;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Post;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class FindEndpoints {
    public static final List<Class<? extends Annotation>> ALLOWED_METHODS = Arrays.asList(Get.class, Post.class);

    public static Map<Class<?>, List<Map<String, Class<?>>>> find(List<Class<?>> controllers) {
        Map<Class<?>, List<Map<String, Class<?>>>> endpoints = new HashMap<>();
        for (Class<? extends Annotation> httpMethod : ALLOWED_METHODS) {
            endpoints.put(httpMethod, new ArrayList<>());
            for (Class<?> controller : controllers) {
                final var baseEndPoint = controller.getAnnotation(Controller.class).path();
                Arrays.stream(controller.getMethods()).forEach(m -> {
                    var endpoint = getEndpoints(baseEndPoint, m, controller, httpMethod);
                    if (endpoint != null) {
                        endpoints.get(httpMethod).add(endpoint);
                    }
                });
            }
        }
        return endpoints;
    }

    public static Map<String, Class<?>> getEndpoints(String baseEndpoint, Method method, Class<?> controller, Class<? extends Annotation> httpMethod) {
        Annotation annotation = method.getAnnotation(httpMethod);
        String path = null;
        if (annotation == null) {
            return null;
        }
        if (annotation instanceof Get) {
            path = ((Get) annotation).path();
        } else if (annotation instanceof Post){
            path = ((Post) annotation).path();
        }

        var map = new HashMap<String, Class<?>>();
        var endpoint = baseEndpoint + path;
        map.put(endpoint, controller);
        return map;
    }
}
