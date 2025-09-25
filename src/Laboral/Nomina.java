package Laboral;

public class Nomina {

    private static final int SUELDO_BASE[] = {50000, 70000, 90000, 110000, 130000, 150000,
            170000, 190000, 210000, 230000};

    public static double sueldo(Empleado e) {
        int categoria = e.getCategoria();
        return (SUELDO_BASE[categoria - 1] + 5000 * e.getAnyos());
    }

}
