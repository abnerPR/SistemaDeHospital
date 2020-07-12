// Programador: Abner Adolfo Piox Rompich
// Control de Versiones
// Fecha de Creacion: 08/07/2019

package org.abnerpiox.bean;


public class Horario {
   private int codigoHorario;
   private String horarioInicio;
   private String horarioSalida;
   private Boolean lunes;
   private Boolean martes;
   private Boolean miercoles;
   private Boolean jueves;
   private Boolean viernes;

    public Horario() {
    }

    public Horario(int codigoHorario, String horarioInicio, String horarioSalida, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, Boolean viernes) {
        this.codigoHorario = codigoHorario;
        this.horarioInicio = horarioInicio;
        this.horarioSalida = horarioSalida;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
    }

    public int getCodigoHorario() {
        return codigoHorario;
    }

    public void setCodigoHorario(int codigoHorario) {
        this.codigoHorario = codigoHorario;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(String horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    public Boolean getLunes() {
        return lunes;
    }

    public void setLunes(Boolean lunes) {
        this.lunes = lunes;
    }

    public Boolean getMartes() {
        return martes;
    }

    public void setMartes(Boolean martes) {
        this.martes = martes;
    }

    public Boolean getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(Boolean miercoles) {
        this.miercoles = miercoles;
    }

    public Boolean getJueves() {
        return jueves;
    }

    public void setJueves(Boolean jueves) {
        this.jueves = jueves;
    }

    public Boolean getViernes() {
        return viernes;
    }

    public void setViernes(Boolean viernes) {
        this.viernes = viernes;
    }
   
    public String toString(){
        return getCodigoHorario() + " | " + getHorarioInicio() + ", " + getHorarioSalida() + ", " + getLunes() + ", " + getMartes() + ", "
                                     + getMiercoles() + ", " + getJueves() + ", " + getViernes();
    }
   
    
}
