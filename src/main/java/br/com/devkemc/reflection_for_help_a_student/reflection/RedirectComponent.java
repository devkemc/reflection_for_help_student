package br.com.devkemc.reflection_for_help_a_student.reflection;

import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.components.Controller;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Get;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@WebServlet("/*")
public class RedirectComponent extends HttpServlet {

    private List<Class<?>> controllers;
    private Map<Class<?>, List<Map<String, Class<?>>>> mapRoutes;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var url = req.getPathInfo();
        var endpoint = this.getEndpointsForMethodHttp(Get.class);
        if (endpoint.isEmpty()) {
            resp.sendError(404);
            return;
        }
        var controller = getControllerForEndpoint(url, endpoint.get());
        if (controller.isEmpty()) {
            resp.sendError(404);
            return;
        }
        var urlController = controller.get().getAnnotation(Controller.class).path();

        var methodOptional = Arrays.stream(controller.get().getMethods()).filter(m -> {
            if (m.getAnnotation(Get.class) == null) {
                return false;
            }
            return (urlController + m.getAnnotation(Get.class).path()).equals(url);

        }).findFirst();

        if (methodOptional.isEmpty()) {
            resp.sendError(404);
            return;
        }
        var method = methodOptional.get();

        try {
            var response = method.invoke(controller.get().getDeclaredConstructor().newInstance());
            resp.getWriter().write(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        final List<Class<?>> classList;
        try {
            classList = FindClass.find(new URI("br/com/devkemc/reflection_for_help_a_student"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.controllers = FindClass.getControllers(classList);
        this.mapRoutes = FindEndpoints.find(controllers);
    }

    private Optional<Class<?>> getControllerForEndpoint(String url, List<Map<String, Class<?>>> endpoints){
         var endpoint = endpoints.stream().filter(e -> e.containsKey(url)).findFirst();
        return endpoint.map(stringClassMap -> stringClassMap.get(url));
    }

    private Optional<List<Map<String, Class<?>>>> getEndpointsForMethodHttp(Class<?> methodHttp) {
        return Optional.of(mapRoutes.get(methodHttp));
    }

    private Optional<Method> getMethodForMethodHttpAndEndpoint(Class<?> controller, Class<?> methodHttp, String endpoint) {
        return Arrays.stream(controller.getMethods()).filter(m -> {
            if (m.getAnnotation(methodHttp) == null) {
                return false;
            }
            return (urlController + m.getAnnotation(Get.class).path()).equals(url);
        }).findFirst();
    }
}
