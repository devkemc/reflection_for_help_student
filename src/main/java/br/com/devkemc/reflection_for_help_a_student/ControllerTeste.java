package br.com.devkemc.reflection_for_help_a_student;

import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.components.Controller;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Get;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Post;

@Controller(path = "/")
public class ControllerTeste {
    private String message;

    @Get
    public String helloWorld(){
        return "Cliente Encontrado";
    }

    @Post
    public String teste(){
        return "asdfsa";
    }
}