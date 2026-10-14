
package ni.uam.edu.tecnoasistencia.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaTecnica {

    private Integer id;
    private Cliente cliente;
    private LocalDate fecha;
    private LocalTime hora;
    private String tipoServicio;
    private String descripcion;
    private String estado;

    public CitaTecnica() {
    }

    public CitaTecnica(Integer id, Cliente cliente, LocalDate fecha,
                       LocalTime hora, String tipoServicio,
                       String descripcion, String estado) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoServicio = tipoServicio;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return tipoServicio + " - " + fecha;
    }
}
