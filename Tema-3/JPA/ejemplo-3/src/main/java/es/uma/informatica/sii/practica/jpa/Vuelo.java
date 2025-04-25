package es.uma.informatica.sii.practica.jpa;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;
import java.util.Set;

@Entity
public class Vuelo {
    @Id
    private Long id;
    @Column(name = "codigo_iata")
    private String codigo;
    @Column(name = "origen_iata")
    private String aeropuertoOrigen;
    @Column(name = "destino_iata")
    private String aeropuertoDestino;
    @Column(name = "fecha_salida")
    private Date fechaLocalSalida;
    @Column(name = "hora_salida")
    private Time horaLocalSalida;
    @Column(name = "fecha_llegada")
    private Date fechaLocalLlegada;
    @Column(name = "hora_llegada")
    private Time horaLocalLlegada;
    @OneToMany(mappedBy = "vuelo")
    private Set<Plaza> plazas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAeropuertoOrigen() {
        return aeropuertoOrigen;
    }

    public void setAeropuertoOrigen(String aeropuertoOrigen) {
        this.aeropuertoOrigen = aeropuertoOrigen;
    }

    public String getAeropuertoDestino() {
        return aeropuertoDestino;
    }

    public void setAeropuertoDestino(String aeropuertoDestino) {
        this.aeropuertoDestino = aeropuertoDestino;
    }

    public Date getFechaLocalSalida() {
        return fechaLocalSalida;
    }

    public void setFechaLocalSalida(Date fechaLocalSalida) {
        this.fechaLocalSalida = fechaLocalSalida;
    }

    public Time getHoraLocalSalida() {
        return horaLocalSalida;
    }

    public void setHoraLocalSalida(Time horaLocalSalida) {
        this.horaLocalSalida = horaLocalSalida;
    }

    public Date getFechaLocalLlegada() {
        return fechaLocalLlegada;
    }

    public void setFechaLocalLlegada(Date fechaLocalLlegada) {
        this.fechaLocalLlegada = fechaLocalLlegada;
    }

    public Time getHoraLocalLlegada() {
        return horaLocalLlegada;
    }

    public void setHoraLocalLlegada(Time horaLocalLlegada) {
        this.horaLocalLlegada = horaLocalLlegada;
    }

    public Set<Plaza> getPlazas() {
        return plazas;
    }

    public void setPlazas(Set<Plaza> plazas) {
        this.plazas = plazas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vuelo vuelo = (Vuelo) o;
        return Objects.equals(id, vuelo.id) && Objects.equals(codigo, vuelo.codigo) && Objects.equals(aeropuertoOrigen, vuelo.aeropuertoOrigen) && Objects.equals(aeropuertoDestino, vuelo.aeropuertoDestino) && Objects.equals(fechaLocalSalida, vuelo.fechaLocalSalida) && Objects.equals(horaLocalSalida, vuelo.horaLocalSalida) && Objects.equals(fechaLocalLlegada, vuelo.fechaLocalLlegada) && Objects.equals(horaLocalLlegada, vuelo.horaLocalLlegada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, aeropuertoOrigen, aeropuertoDestino, fechaLocalSalida, horaLocalSalida, fechaLocalLlegada, horaLocalLlegada);
    }
}
