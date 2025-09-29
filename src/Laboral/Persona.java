package Laboral;

public class Persona {

    // ATRIBUTOS
    public String nombre;
    public String dni;
    public char sexo;


    // CONSTRUCTORES
    public Persona(char sexo, String dni, String nombre) {
        this.sexo = sexo;
        this.dni = dni;
        this.nombre = nombre;
    }

    public Persona(String nombre, char sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String Imprime() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", dni='" + dni + '\'' +
                '}';
    }
}