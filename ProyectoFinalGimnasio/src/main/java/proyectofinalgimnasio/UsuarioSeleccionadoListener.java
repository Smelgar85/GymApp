/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package proyectofinalgimnasio;

/**
 *
 * @author Sebastián Melgar Marín
 */
public interface UsuarioSeleccionadoListener {

    /**
     *
     * @param nombre
     * @param apellidos
     * @param dni
     */
    void onUsuarioSeleccionado(String nombre, String apellidos, String dni);
}

