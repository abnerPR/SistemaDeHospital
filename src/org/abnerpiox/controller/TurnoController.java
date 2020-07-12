 /*
Programador: Abner Adolfo Piox Rompich 2018137
Control de Versiones 
Fecha de Creacion: 13/07/2019
 */
package org.abnerpiox.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.abnerpiox.bean.MedicoEspecialidad;
import org.abnerpiox.bean.Paciente;
import org.abnerpiox.bean.ResponsableTurno;
import org.abnerpiox.bean.Turno;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;

public class TurnoController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ACTUALIZAR,ELIMINAR,REPORTE,CANCELAR,GUARDAR,EDITAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Turno> listaTurno;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fechaTurno;
    private DatePicker fechaCita;
    
    @FXML private GridPane grpFechaTurno; 
    @FXML private GridPane grpFechaCita;
    @FXML private TextField txtValorCita;
    @FXML private ComboBox cmbCodigoMedicoEspecialidad;
    @FXML private ComboBox cmbCodigoResponsableTurno;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableView tblTurno;
    @FXML private TableColumn colCodigoTurno;
    @FXML private TableColumn colFechaTurno;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colValorCita;
    @FXML private TableColumn colCodigoMedicoEspecialidad;
    @FXML private TableColumn colCodigoResponsableTurno;
    @FXML private TableColumn colCodigoPaciente;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fechaTurno = new DatePicker(Locale.ENGLISH);
        fechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaTurno.getCalendarView().todayButtonTextProperty().set("Today");
        fechaTurno.getCalendarView().setShowWeeks(false);
        fechaTurno.getStylesheets().add("/org/abnerpiox/resource/DatePicker.css");
        grpFechaTurno.add(fechaTurno, 0, 0);
        
        fechaCita = new DatePicker(Locale.ENGLISH);
        fechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaCita.getCalendarView().todayButtonTextProperty().set("Today");
        fechaCita.getCalendarView().setShowWeeks(false);
        fechaCita.getStylesheets().add("/org/abnerpiox/resource/DatePicker.css");
        grpFechaCita.add(fechaCita, 0, 0);
        
        cargarDatos();
        cmbCodigoMedicoEspecialidad.setItems(getMedicoEspecialidades());
        cmbCodigoResponsableTurno.setItems(getResponsableTurnos());
        cmbCodigoPaciente.setItems(getPacientes());
        
        soloNumeros(txtValorCita);
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
    
    public ObservableList<ResponsableTurno> getResponsableTurnos(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_ListarResponsablesTurnos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                                      resultado.getString("nombreResponsable"),
                                      resultado.getString("apellidosResponsable"),
                                      resultado.getString("telefonoPersonal"),
                                      resultado.getInt("codigoArea"),
                                      resultado.getInt("codigoCargo")));
            }       
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaResponsableTurno = FXCollections.observableList(lista);
    }
    
    public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("DPI"),
                        resultado.getString("apellidos"),
                        resultado.getString("nombres"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getInt("edad"),
                        resultado.getString("direccion"),
                        resultado.getString("ocupacion"),
                        resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPaciente = FXCollections.observableList(lista);
    }
    
    public void cargarDatos(){
        tblTurno.setItems(getTurnos());
        colCodigoTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurno"));
        colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaCita"));
        colValorCita.setCellValueFactory(new PropertyValueFactory<Turno, Float>("valorCita"));
        colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoMedicoEspecialidad"));
        colCodigoResponsableTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoResponsableTurno"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoPaciente"));
    }
    
    public ObservableList<Turno> getTurnos(){
        ArrayList<Turno> lista = new ArrayList<Turno>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurnos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Turno(resultado.getInt("codigoTurno"),
                                    resultado.getDate("fechaTurno"),
                                    resultado.getDate("fechaCita"),
                                    resultado.getFloat("valorCita"),
                                    resultado.getInt("codigoMedicoEspecialidad"),
                                    resultado.getInt("codigoResponsableTurno"),
                                    resultado.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTurno = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
     if(tblTurno.getSelectionModel().getSelectedItem() != null){
         fechaTurno.selectedDateProperty().set(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaTurno());
         fechaCita.selectedDateProperty().set(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaCita());
         txtValorCita.setText(String.valueOf(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getValorCita()));
         cmbCodigoMedicoEspecialidad.getSelectionModel().select(buscarMedicoEspecialidad(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
         cmbCodigoResponsableTurno.getSelectionModel().select(buscarResponsableTurno(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
         cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        }else
            JOptionPane.showMessageDialog(null,"Registro no valido");
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
    
    public ResponsableTurno buscarResponsableTurno(int codigoResponsableTurno){
        ResponsableTurno resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResponsableTurno(?)}");
            procedimiento.setInt(1, codigoResponsableTurno);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ResponsableTurno(registro.getInt("codigoResponsableTurno"),
                                               registro.getString("nombreResponsable"),
                                               registro.getString("apellidosResponsable"),
                                               registro.getString("telefonoPersonal"),
                                               registro.getInt("codigoArea"),
                                               registro.getInt("codigoCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                         registro.getString("DPI"),
                                         registro.getString("apellidos"),
                                         registro.getString("nombres"),
                                         registro.getDate("fechaNacimiento"),
                                         registro.getInt("edad"),
                                         registro.getString("direccion"),
                                         registro.getString("ocupacion"),
                                         registro.getString("sexo"));
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
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                cargarDatos();
                break;
        }    
    }
    
    public void guardar(){
            if(fechaTurno.getSelectedDate() != null && fechaCita.getSelectedDate() != null && txtValorCita.getText().length() != 0 && cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem() != null && cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem() != null && cmbCodigoPaciente.getSelectionModel().getSelectedItem() != null){
               Turno registro = new Turno();
               registro.setFechaTurno(fechaTurno.getSelectedDate());
               registro.setFechaCita(fechaCita.getSelectedDate());
               registro.setValorCita(Float.parseFloat(txtValorCita.getText()));
               registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
               registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
               registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
               
               try{
                   PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?,?,?,?)}");
                   procedimiento.setDate(1, new java.sql.Date(registro.getFechaTurno().getTime()));
                   procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
                   procedimiento.setFloat(3, registro.getValorCita());
                   procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
                   procedimiento.setInt(5, registro.getCodigoResponsableTurno());
                   procedimiento.setInt(6, registro.getCodigoPaciente());
                   procedimiento.execute();
                   listaTurno.add(registro);
                   
                   desactivarControles();
                   limpiarControles();
                   btnNuevo.setText("Nuevo");
                   btnEliminar.setText("Eliminar");
                   btnEditar.setDisable(false);
                   btnReporte.setDisable(false);
                   tipoDeOperacion = operaciones.NINGUNO;
                   
               }catch(Exception e){
                   e.printStackTrace();
               }
            }else 
                JOptionPane.showMessageDialog(null,"Error, faltan datos para ingresar");
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
                    int resultado = JOptionPane.showConfirmDialog(null,"¿Desea eliminar el registro?","Eliminar Turno",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    
                    if(resultado == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurno(?)}");
                            procedimiento.setInt(1, ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno());
                            procedimiento.execute();
                            listaTurno.remove(tblTurno.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
        }
        
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTurno.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    activarControles();
                    cmbCodigoMedicoEspecialidad.setDisable(true);
                    cmbCodigoResponsableTurno.setDisable(true);
                    cmbCodigoPaciente.setDisable(true);
                }else 
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                break;
                
            case ACTUALIZAR:
                
                actualizar();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTurno(?,?,?,?,?,?,?)}");
            Turno registro = (Turno)tblTurno.getSelectionModel().getSelectedItem();
            registro.setFechaTurno(fechaTurno.getSelectedDate());
            registro.setFechaCita(fechaCita.getSelectedDate());
            registro.setValorCita(Float.parseFloat(txtValorCita.getText()));
            registro.setCodigoMedicoEspecialidad(((MedicoEspecialidad)cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
            registro.setCodigoResponsableTurno(((ResponsableTurno)cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
            registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            
            procedimiento.setInt(1, registro.getCodigoTurno());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaTurno().getTime()));
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setFloat(4, registro.getValorCita());
            procedimiento.setInt(5, registro.getCodigoMedicoEspecialidad());
            procedimiento.setInt(6, registro.getCodigoResponsableTurno());
            procedimiento.setInt(7, registro.getCodigoPaciente());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                limpiarControles();
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
    }
   }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",null);
        GenerarReporte.mostrarReporte("ReporteTurnos.jasper","Reporte de Turnos", parametros);  
    }
    
    public void desactivarControles(){
        grpFechaTurno.setDisable(true);
        grpFechaCita.setDisable(true);
        txtValorCita.setEditable(false);
        cmbCodigoMedicoEspecialidad.setDisable(true);
        cmbCodigoResponsableTurno.setDisable(true);
        cmbCodigoPaciente.setDisable(true);
        tblTurno.setDisable(false);
    }
    
    public void activarControles(){
        grpFechaTurno.setDisable(false);
        grpFechaCita.setDisable(false);
        txtValorCita.setEditable(true);
        cmbCodigoMedicoEspecialidad.setDisable(false);
        cmbCodigoResponsableTurno.setDisable(false);
        cmbCodigoPaciente.setDisable(false);
        tblTurno.setDisable(true);
        
    }
    
    public void limpiarControles(){
        
        fechaTurno.setSelectedDate(null);
        fechaCita.setSelectedDate(null);
        txtValorCita.setText("");
        cmbCodigoMedicoEspecialidad.getSelectionModel().clearSelection();
        cmbCodigoMedicoEspecialidad.setValue(null);
        cmbCodigoResponsableTurno.getSelectionModel().clearSelection();
        cmbCodigoResponsableTurno.setValue(null);
        cmbCodigoPaciente.getSelectionModel().clearSelection();
        cmbCodigoPaciente.setValue(null);
        tblTurno.getSelectionModel().clearSelection();
    }
    
    public void soloNumeros(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                int valor = 9;
                char ingresado = e.getCharacter().charAt(0);
                
                if(!Character.isDigit(ingresado) && ingresado != '\b' && ingresado != '\t' && ingresado != '.'){
                    e.consume();
                    JOptionPane.showMessageDialog(null,"Solamente puede escribir Numeros");
                }if(ingresado == '.'){
                    valor = 11;
                    if(txtValorCita.getText().length() > 11){
                        e.consume();
                    }
                }else if(txtValorCita.getText().length() > valor){
                    e.consume();
                }
            }
   });
                }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    
}
