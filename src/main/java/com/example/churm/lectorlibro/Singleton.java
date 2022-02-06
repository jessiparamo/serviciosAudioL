package com.example.churm.lectorlibro;

public class Singleton {
    // Esta será la instancia única de esta clase
    private static Singleton INSTANCIA = new Singleton();      // El constructor es private para evitar su acceso desde fuera.  private Singleton() {}

    // Método para obtener la única instancia de la clase
    public static Singleton getInstancia() {
        return INSTANCIA;
    }
    Singleton referencia = Singleton.getInstancia();
}
