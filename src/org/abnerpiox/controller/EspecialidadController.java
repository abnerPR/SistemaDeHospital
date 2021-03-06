
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.abnerpiox.bean.Especialidad;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;


public class EspecialidadController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private  operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colEspecialidades;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        soloLetras(txtNombreEspecialidad);
    }
    
    public void soloLetras(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                char caracter = e.getCharacter().charAt(0);
                
            if(!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b' && caracter != '\t'){
                e.consume();
                JOptionPane.showMessageDialog(null,"Solamente puede escribir letras");
            }
            }    
        });  
    }
    
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidades());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colEspecialidades.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("nombreEspecialidad"));
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
    
        public void seleccionarElemento(){
            if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
            txtNombreEspecialidad.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
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
                    if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                         int respuesta = JOptionPane.showConfirmDialog(null,"¿Desea eliminar el registro?","Eliminar Especialidad",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                            if(respuesta == JOptionPane.YES_OPTION){
                                try{
                                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                                    procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                                    procedimiento.execute();
                                    listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedItem());
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
        
        public void actualizar(){
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
                Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
                registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
                
                procedimiento.setInt(1, registro.getCodigoEspecialidad());
                procedimiento.setString(2, registro.getNombreEspecialidad());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                        activarControles();
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
        GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper","Reporte de Medicos", parametros);
    }
        
        public void guardar(){
            if(txtNombreEspecialidad.getText().length() != 0){
            Especialidad registro = new Especialidad();
            registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
            
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarEspecialidad(?)}"); // Un signo de interrogacion por cada dato a ingresar
           procedimiento.setString(1, registro.getNombreEspecialidad());
           procedimiento.execute();
           listaEspecialidad.add(registro);
           
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
    
    public void desactivarControles(){
        txtNombreEspecialidad.setEditable(false);
        tblEspecialidades.setDisable(false);
        
    }

    public void activarControles(){
        txtNombreEspecialidad.setEditable(true);
        tblEspecialidades.setDisable(true);
        
    }
    
    public void limpiarControles(){
        txtNombreEspecialidad.setText("");
        tblEspecialidades.getSelectionModel().clearSelection();
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
