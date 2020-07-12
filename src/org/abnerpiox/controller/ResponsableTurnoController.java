/*
Programador: Abner Adolfo Piox Rompich 2018137
Control de versiones:
Fecha de Creación: 08/07/2019
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.abnerpiox.bean.Area;
import org.abnerpiox.bean.Cargo;
import org.abnerpiox.bean.ResponsableTurno;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;

public class ResponsableTurnoController implements Initializable{
    
    private enum operaciones{NINGUNO,NUEVO,ELIMINAR,ACTUALIZAR,CANCELAR,GUARDAR,EDITAR}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargo> listaCargo;
    
    @FXML private TextField txtNombreResponsable;
    @FXML private TextField txtApellidoResponsable;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private ComboBox cmbCodigoArea;
    @FXML private ComboBox cmbCodigoCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableView tblResponsableTurno;
    @FXML private TableColumn colCodigoResponsable;
    @FXML private TableColumn colNombreResponsable;
    @FXML private TableColumn colApellidoResponsable;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colCodigoArea;
    @FXML private TableColumn colCodigoCargo;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
        cmbCodigoCargo.setItems(getCargos());
        soloNumeros(txtTelefonoPersonal);
        soloLetras(txtNombreResponsable);
        soloLetras(txtApellidoResponsable);
    }
    
     public void soloNumeros(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                char caracter = e.getCharacter().charAt(0);
                
                if(!Character.isDigit(caracter) && caracter != '\t' && caracter != '\b'){
                    e.consume();
                    JOptionPane.showMessageDialog(null,"Solamente puede escribir números");
                }
            } 
        });
    }
     
     public void soloLetras(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                char caracter = e.getCharacter().charAt(0);
                
                if(!Character.isLetter(caracter) && caracter != '\b' && caracter != '\t' && !Character.isSpaceChar(caracter)){
                    e.consume();
                    JOptionPane.showMessageDialog(null,"Solamente puede escribir Letras");
                }
            } 
        });
    }
     
     public void cargarDatos(){
         tblResponsableTurno.setItems(getResponsableTurnos());
         colCodigoResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
         colNombreResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreResponsable"));
         colApellidoResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
         colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
         colCodigoArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("codigoArea"));
         colCodigoCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("codigoCargo"));   
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
     
     public ObservableList<Area> getAreas(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"),
                                resultado.getString("nombreArea")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaArea = FXCollections.observableList(lista);
    }
     
    public ObservableList<Cargo> getCargos(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"),
                            resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaCargo = FXCollections.observableArrayList(lista);
    }
    
     public void seleccionarElemento(){
         if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
             txtNombreResponsable.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable());
             txtApellidoResponsable.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
             txtTelefonoPersonal.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
             cmbCodigoArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoArea()));
             cmbCodigoCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoCargo()));
         }else
             JOptionPane.showMessageDialog(null,"Registro no valido");
     }
     
     public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(registro.getInt("codigoArea"),
                                     registro.getString("nombreArea"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
     
    public Cargo buscarCargo(int codigoCargo){
          Cargo resultado = null;
          try{
              PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
              procedimiento.setInt(1, codigoCargo);
              ResultSet registro = procedimiento.executeQuery();
              while(registro.next()){
                  resultado = new Cargo(registro.getInt("codigoCargo"),
                                        registro.getString("nombreCargo"));
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
        }
    }
    
    public void guardar(){
        if(txtNombreResponsable.getText().length() != 0 && txtApellidoResponsable.getText().length() != 0 && txtTelefonoPersonal.getText().length() != 0 && cmbCodigoArea.getSelectionModel().getSelectedItem() != null && cmbCodigoCargo.getSelectionModel().getSelectedItem() != null){
            ResponsableTurno registro = new ResponsableTurno();
            registro.setNombreResponsable(txtNombreResponsable.getText());
            registro.setApellidosResponsable(txtApellidoResponsable.getText());
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
                procedimiento.setString(1, registro.getNombreResponsable());
                procedimiento.setString(2, registro.getApellidosResponsable());
                procedimiento.setString(3, registro.getTelefonoPersonal());
                procedimiento.setInt(4, registro.getCodigoArea());
                procedimiento.setInt(5, registro.getCodigoCargo());
                procedimiento.execute();
                listaResponsableTurno.add(registro);
                
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
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar el registro?","Eliminar Telefono Medico",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                            procedimiento.setInt(1, ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                            procedimiento.execute();
                            listaResponsableTurno.remove(tblResponsableTurno.getSelectionModel().getSelectedItem());
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
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    cmbCodigoArea.setDisable(true);
                    cmbCodigoCargo.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
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
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?,?,?,?,?,?)}");
            ResponsableTurno registro = (ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem();
            registro.setNombreResponsable(txtNombreResponsable.getText());
            registro.setApellidosResponsable(txtApellidoResponsable.getText());
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setCodigoArea(((Area)cmbCodigoArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargo)cmbCodigoCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidosResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",null);
        GenerarReporte.mostrarReporte("ReporteResponsableTurno.jasper","Reporte de ResponsableTurno", parametros);
        
    }
    
    public void activarControles(){
        txtNombreResponsable.setEditable(true);
        txtApellidoResponsable.setEditable(true);
        txtTelefonoPersonal.setEditable(true);
        cmbCodigoArea.setDisable(false);
        cmbCodigoCargo.setDisable(false);
        tblResponsableTurno.setDisable(true);
    }
    
    public void desactivarControles(){
       txtNombreResponsable.setEditable(false);
       txtApellidoResponsable.setEditable(false);
       txtTelefonoPersonal.setEditable(false);
       cmbCodigoArea.setDisable(true);
       cmbCodigoCargo.setDisable(true);
       tblResponsableTurno.setDisable(false); 
    }
    
    public void limpiarControles(){
       txtNombreResponsable.setText("");
       txtApellidoResponsable.setText("");
       txtTelefonoPersonal.setText("");
       cmbCodigoArea.getSelectionModel().clearSelection();
       cmbCodigoArea.setValue(null);
       cmbCodigoCargo.getSelectionModel().clearSelection();
       cmbCodigoCargo.setValue(null);
       tblResponsableTurno.getSelectionModel().clearSelection();
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
