// Programador: Abner Adolfo Piox Rompich
// Control de Versiones
// Fecha de Creacion: 08/07/2019

package org.abnerpiox.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.abnerpiox.bean.Horario;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;

public class HorariosController implements Initializable{
    private enum operaciones{NUEVO,GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horario> listaHorarios;
    
    @FXML private TextField txtHorarioInicio;
    @FXML private TextField txtHorarioSalida;
    @FXML private CheckBox chkLunes;
    @FXML private CheckBox chkMartes;
    @FXML private CheckBox chkMiercoles;
    @FXML private CheckBox chkJueves;
    @FXML private CheckBox chkViernes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnEditar;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigoHorario;
    @FXML private TableColumn colHorarioInicio;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
      
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        soloNumeros(txtHorarioInicio);
        soloNumeros(txtHorarioSalida);
    }
    
    public void soloNumeros(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                char caracter = e.getCharacter().charAt(0);
                
                if(!Character.isDigit(caracter) && caracter != ':' && caracter != '\b' && caracter != '\t'){
                    e.consume();
                    JOptionPane.showMessageDialog(null,"Solamente puede escribir números");
                }
            }
            
        });
    }
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colHorarioInicio.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioInicio"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horario,String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
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
    
    public void seleccionarElemento(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            txtHorarioInicio.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());
            txtHorarioSalida.setText(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
            chkLunes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getLunes());
            chkMartes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMartes());
            chkMiercoles.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles());
            chkJueves.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getJueves());
            chkViernes.setSelected(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getViernes());
        }else 
            JOptionPane.showMessageDialog(null,"Registro no valido");
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
        if(txtHorarioInicio.getText().length() != 0 && txtHorarioSalida.getText().length() != 0){
            Horario registro = new Horario();
            registro.setHorarioInicio(txtHorarioInicio.getText());
            registro.setHorarioSalida(txtHorarioSalida.getText());
            registro.setLunes(chkLunes.isSelected());
            registro.setMartes(chkMartes.isSelected());
            registro.setMiercoles(chkMiercoles.isSelected());
            registro.setJueves(chkJueves.isSelected());
            registro.setViernes(chkViernes.isSelected());
            
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioInicio());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.getLunes());
            procedimiento.setBoolean(4, registro.getMartes());
            procedimiento.setBoolean(5, registro.getMiercoles());
            procedimiento.setBoolean(6, registro.getJueves());
            procedimiento.setBoolean(7, registro.getViernes());
            procedimiento.execute();
            
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
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el registro","Eliminar Horario",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listaHorarios.remove(tblHorarios.getSelectionModel().getSelectedItem());
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
    
    public void editar(){
        switch(tipoDeOperacion){
        case NINGUNO:
            if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            activarControles();
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            btnNuevo.setDisable(true);
            btnEliminar.setDisable(true);
            tipoDeOperacion = operaciones.ACTUALIZAR;
            }else
            JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
        break;
        
        case ACTUALIZAR:
            actualizar();
            cargarDatos();
            desactivarControles();
            limpiarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setDisable(false);
            btnEliminar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
        break;         
    }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_EditarHorario(?,?,?,?,?,?,?,?)}");
            Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();
            registro.setHorarioInicio(txtHorarioInicio.getText());
            registro.setHorarioSalida(txtHorarioSalida.getText());
            registro.setLunes(chkLunes.isSelected());
            registro.setMartes(chkMartes.isSelected());
            registro.setMiercoles(chkMiercoles.isSelected());
            registro.setJueves(chkJueves.isSelected());
            registro.setViernes(chkViernes.isSelected());
            
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioInicio());
            procedimiento.setString(3, registro.getHorarioSalida());
            procedimiento.setBoolean(4, registro.getLunes());
            procedimiento.setBoolean(5, registro.getMartes());
            procedimiento.setBoolean(6, registro.getMiercoles());
            procedimiento.setBoolean(7, registro.getJueves());
            procedimiento.setBoolean(8, registro.getViernes());
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
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
        }
        
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",null);
        GenerarReporte.mostrarReporte("ReporteHorarios.jasper","Reporte de Horarios", parametros);
        
    }
    
    public void activarControles(){
        txtHorarioInicio.setEditable(true);
        txtHorarioSalida.setEditable(true);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);
        tblHorarios.setDisable(true);
    }
    
    public void desactivarControles(){
        txtHorarioInicio.setEditable(false);
        txtHorarioSalida.setEditable(false);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true);
        tblHorarios.setDisable(false);
    }
    
    public void limpiarControles(){
        txtHorarioInicio.setText("");
        txtHorarioSalida.setText("");
        chkLunes.setSelected(false);
        chkMartes.setSelected(false);
        chkMiercoles.setSelected(false);
        chkJueves.setSelected(false);
        chkViernes.setSelected(false);
        tblHorarios.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPricipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
