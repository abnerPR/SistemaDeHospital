 /*
Programador: Abner Adolfo Piox Rompich 2018137
Control de Versiones 
Fecha de Creacion: 13/07/2019
 */

package org.abnerpiox.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.abnerpiox.bean.Especialidad;
import org.abnerpiox.bean.Horario;
import org.abnerpiox.bean.Medico;
import org.abnerpiox.bean.MedicoEspecialidad;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.sistema.Principal;

public class MedicoEspecialidadController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ACTUALIZAR,CANCELAR,REPORTE,ELIMINAR,EDITAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Horario> listaHorarios;
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private ComboBox cmbCodigoHorario;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private TableColumn colCodigoMedicoEspecialidad;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colCodigoHorario;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCodigoMedico.setItems(getMedicos());
        cmbCodigoHorario.setItems(getHorarios());
        cmbCodigoEspecialidad.setItems(getEspecialidades());
        cargarDatos();
    }
    
    public ObservableList<Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),
                                resultado.getInt("licenciaMedica"),
                                resultado.getString("nombres"),
                                resultado.getString("apellidos"),
                                resultado.getString("horaEntrada"),
                                resultado.getString("horaSalida"),
                                resultado.getInt("turnoMaximo"),
                                resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedico = FXCollections.observableList(lista);
    }
    
    public ObservableList<Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_ListarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Horario(resultado.getInt("codigoHorario"),
                                      resultado.getString("horarioInicio"),
                                      resultado.getString("horarioSalida"),
                                      resultado.getBoolean("lunes"),
                                      resultado.getBoolean("martes"),
                                      resultado.getBoolean("miercoles"),
                                      resultado.getBoolean("jueves"),
                                      resultado.getBoolean("viernes")));
            }       
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaHorarios = FXCollections.observableList(lista);
    }
    
    public ObservableList<Especialidad> getEspecialidades(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades}");
                ResultSet resultado = procedimiento.executeQuery();
                
                while(resultado.next()){
                    lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                                resultado.getString("nombreEspecialidad")));
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidades());
        colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedicoEspecialidad"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedico"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoEspecialidad"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoHorario"));
    }
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidades(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicosEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                                                 resultado.getInt("codigoMedico"),
                                                 resultado.getInt("codigoEspecialidad"),
                                                 resultado.getInt("codigoHorario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedicoEspecialidad = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
            cmbCodigoMedico.getSelectionModel().select(buscarMedico(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
            cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
            cmbCodigoHorario.getSelectionModel().select(buscarHorario(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
        }else
            JOptionPane.showMessageDialog(null,"Registro no valido");
    }
    
    public Medico buscarMedico(int codigoMedico) {
        Medico resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Medico(registro.getInt("codigoMedico"),
                        registro.getInt("licenciaMedica"),
                        registro.getString("nombres"),
                        registro.getString("apellidos"),
                        registro.getString("horaEntrada"),
                        registro.getString("horaSalida"),
                        registro.getInt("turnoMaximo"),
                        registro.getString("sexo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }
    
    public Horario buscarHorario(int codigoHorario){
        Horario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_BuscarHorario(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horario(registro.getInt("codigoHorario"),
                                        registro.getString("horarioInicio"),
                                        registro.getString("horarioSalida"),
                                        registro.getBoolean("lunes"),
                                        registro.getBoolean("martes"),
                                        registro.getBoolean("miercoles"),
                                        registro.getBoolean("jueves"),
                                        registro.getBoolean("Viernes"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
            Especialidad resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
                procedimiento.setInt(1, codigoEspecialidad);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new Especialidad(registro.getInt("codigoEspecialidad"),
                                                 registro.getString("nombreEspecialidad"));
                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return resultado;
        }
    
    public MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
            MedicoEspecialidad resultado = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicoEspecialidad(?)}");
                procedimiento.setInt(1, codigoMedicoEspecialidad);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),
                                                 registro.getInt("codigoMedico"),
                                                 registro.getInt("codigoEspecialidad"),
                                                 registro.getInt("codigoHorario"));
                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return resultado;
        }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                tipoDeOperacion = operaciones.GUARDAR;
                break;
              
            case GUARDAR:
                guardar();
                cargarDatos();    
        }
        
    }
    
    public void guardar(){
        if(cmbCodigoMedico.getSelectionModel().getSelectedItem() != null && cmbCodigoEspecialidad.getSelectionModel().getSelectedItem() != null && cmbCodigoHorario.getSelectionModel().getSelectedItem() != null){
            MedicoEspecialidad registro = new MedicoEspecialidad();
            registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
            registro.setCodigoHorario(((Horario)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
            
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidad(?,?,?)}");
                procedimiento.setInt(1,registro.getCodigoMedico());
                procedimiento.setInt(2, registro.getCodigoEspecialidad());
                procedimiento.setInt(3, registro.getCodigoHorario());
                procedimiento.execute();
                listaMedicoEspecialidad.add(registro);
                
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoDeOperacion = operaciones.NINGUNO;
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,"Error, faltan datos para ingresar");
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
                
            default:
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Medico Especialidad",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicoEspecialidad(?)}");
                            procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                            procedimiento.execute();
                            listaMedicoEspecialidad.remove(tblMedicoEspecialidad.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            tipoDeOperacion = operaciones.NINGUNO;
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else
                    JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                    break;
        }
        
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR; 
                }else
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
            break;
                
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedicoEspecialidad(?,?,?,?)}");
            MedicoEspecialidad registro = (MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem();
            registro.setCodigoHorario(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
            registro.setCodigoHorario(((Horario)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());  
            
            procedimiento.setInt(1, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(2, registro.getCodigoMedico());
            procedimiento.setInt(3, registro.getCodigoEspecialidad());
            procedimiento.setInt(4, registro.getCodigoHorario());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                break;
        }
    }
    
    public void activarControles(){
        cmbCodigoMedico.setDisable(false);
        cmbCodigoEspecialidad.setDisable(false);
        cmbCodigoHorario.setDisable(false);
        tblMedicoEspecialidad.setDisable(true);
    }
    
    public void desactivarControles(){
        cmbCodigoMedico.setDisable(true);
        cmbCodigoEspecialidad.setDisable(true);
        cmbCodigoHorario.setDisable(true);
        tblMedicoEspecialidad.setDisable(false);
    }
    
    public void limpiarControles(){
        cmbCodigoMedico.getSelectionModel().clearSelection();
        cmbCodigoMedico.setValue(null);
        cmbCodigoEspecialidad.getSelectionModel().clearSelection();
        cmbCodigoEspecialidad.setValue(null);
        cmbCodigoHorario.getSelectionModel().clearSelection();
        cmbCodigoHorario.setValue(null);
        tblMedicoEspecialidad.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
