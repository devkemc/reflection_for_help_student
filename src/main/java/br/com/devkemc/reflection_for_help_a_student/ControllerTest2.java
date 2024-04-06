package br.com.devkemc.reflection_for_help_a_student;

import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.components.Controller;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Get;
import br.com.devkemc.reflection_for_help_a_student.reflection.annotations.methods.Post;

@Controller(path = "/clientes")
public class ControllerTest2 {
    private String message;

    @Get(path = "/findById")
    public String helloWorld(){
        return "Hello World";
    }

    @Post(path = "/cadastrar")
    public String teste(){
        return "asdfsa";
    }
}