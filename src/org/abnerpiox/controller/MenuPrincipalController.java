
package org.abnerpiox.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedicos(){
        escenarioPrincipal.ventanaMedicos(); //Para que levante la ventana medicos y lo reconozca
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaPacientes(){
        escenarioPrincipal.ventanaPacientes();
    }
    
    public void ventanaEspecialidades(){
        escenarioPrincipal.ventanaEspecialidades();
    }
    
    public void ventanaAreas(){
        escenarioPrincipal.ventanaAreas();
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorarios();
    }
    
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }
    
    public void ventanaMedicoEspecialidad(){
        escenarioPrincipal.ventanaMedicoEspecialidad();
    }
    
    public void ventanaTurno(){
        escenarioPrincipal.ventanaTurno();
    }
    
    public void ReporteFinal(){
        String codM = JOptionPane.showInputDialog(null,"Ingrese el codigo Medico");
        
        if(codM != null){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",codM);
        GenerarReporte.mostrarReporte("ReporteFinal.jasper","Reporte Final", parametros);
        }
    }
    
    public void ReportePacientes(){
        Map parametros = new HashMap();
        parametros.put("codigoPaciente",null);
        GenerarReporte.mostrarReporte("ReportePacientes.jasper","Reporte De Pacientes", parametros);
    }
    
    public void ReporteMedicos(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",null);
        GenerarReporte.mostrarReporte("ReporteMedicos.jasper","Reporte de Medicos",parametros);
    }
    
}
