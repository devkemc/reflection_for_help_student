package br.com.devkemc.reflection_for_help_a_student.reflection;

import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.components.Controller;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindClass {
    public static List<Class<?>> find(URI baseFile) {
        List<Class<?>> classList = new ArrayList<>();
        try {

            load(baseFile, classList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return classList;
    }

    private static void load(URI uri, List<Class<?>> classList) throws URISyntaxException {
        var baseFile = new File("target/classes/" + uri.getPath());
        loadFile(baseFile, classList);
    }

    private static void loadFile(File file, List<Class<?>> classList) throws URISyntaxException{
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                loadFile(new File(f.getPath()), classList);
            }
        } else if (file.getName().endsWith(".class")) {
            classList.add(loadClass(new URI(file.getPath())));
        }
    }

    private static Class<?> loadClass(URI classPath) {
        try {
            String className = classPath.getPath().replace("target/classes/","").replace(".class", "").replace("/", ".");

            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Class<?>> getControllers(List<Class<?>> classList) {
        return classList.stream().filter(c -> c.isAnnotationPresent(Controller.class)).collect(Collectors.toList());
    }
}
