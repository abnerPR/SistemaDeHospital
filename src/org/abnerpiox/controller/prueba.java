
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
import org.abnerpiox.bean.Prueba;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;

/**
 *
 * @author abpio
 */
public class prueba implements Initializable{
    private Principal principal;
    private enum operaciones {GUARDAR, ACTUALIZAR, NINGUNO, ELIMINAR, CANCELAR, REPORTE};
    private operaciones tipodeoperacion = operaciones.NINGUNO;
    private ObservableList<Prueba> listaPrueba;
    
    @FXML private TextField txtnombre;
    @FXML private TextField txtapellido;
    @FXML private TextField txtedad;
    @FXML private Button nuevo;
    @FXML private Button eliminar;
    @FXML private Button editar;
    @FXML private Button reporte;
    @FXML private TableView tblprueba;
    @FXML private TableColumn colnombre;
    @FXML private TableColumn colapellido;
    @FXML private TableColumn coledad;
    @FXML private ComboBox cmbPrueba;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void keyEvent(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                char ingresado = event.getCharacter().charAt(0);
                
                if(!Character.isLetter(ingresado)){
                    event.consume();
                }
            }
        });
    }
    
    public void cargar(){
        tblprueba.setItems(getPrueba());
        colnombre.setCellValueFactory(new PropertyValueFactory<Prueba, String>("nombre"));
        colapellido.setCellValueFactory(new PropertyValueFactory<Prueba, String>("apellidos"));
        coledad.setCellValueFactory(new PropertyValueFactory<Prueba, Integer>("edad"));
    }
    
    public ObservableList<Prueba> getPrueba(){
        ArrayList<Prueba> lista = new ArrayList();
        
        try {
            PreparedStatement registro = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPrueba()}");
            ResultSet resultado = registro.executeQuery();
            
            while(resultado.next()){
                 lista.add(new Prueba(resultado.getString("nombre"),
                                    resultado.getString("apellidos"),
                                    resultado.getInt("edad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaPrueba = FXCollections.observableArrayList(lista);
              
    }
    
    public Prueba buscarPrueba(int edad){
        Prueba resultado = null;
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar(?)}");
            procedimiento.setInt(1, edad);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Prueba( registro.getString("nombre"),
                                        registro.getString("apellido"),
                                        registro.getInt("edad"));
            }
        } catch (Exception e) {
        }
        
        return resultado;
    }
    
    public void seleccionar(){
        txtnombre.setText(((Prueba)tblprueba.getSelectionModel().getSelectedItem()).getNombre());
        txtapellido.setText(((Prueba)tblprueba.getSelectionModel().getSelectedItem()).getApellido());
        txtedad.setText(String.valueOf(((Prueba)tblprueba.getSelectionModel().getSelectedItem()).getEdad()));
    }
    
    public void nuevo(){
        switch(tipodeoperacion){
            case NINGUNO:
            break;
            
            case GUARDAR:
            break;
        }
    }
    
    public void guardar(){
        Prueba registro = new Prueba();
        registro.setNombre(txtnombre.getText());
        registro.setApellido(txtapellido.getText());
        registro.setEdad(Integer.parseInt(txtedad.getText()));
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPrueba(?,?,?)}");
            procedimiento.setString(1, registro.getNombre());
            procedimiento.setString(2, registro.getApellido());
            procedimiento.setInt(3, registro.getEdad());
            procedimiento.execute();
            listaPrueba.add(registro);
            
            tipodeoperacion = operaciones.NINGUNO;
        } catch (Exception e) {
        }
    }
    
    public void eliminar(){
        switch(tipodeoperacion){
            case NINGUNO:
                
                int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar este registro", "eliminar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if(respuesta == JOptionPane.YES_OPTION){
                    try {
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Eliminar(?)}");
                        procedimiento.setString(1, ((Prueba)tblprueba.getSelectionModel().getSelectedItem()).getNombre());
                        procedimiento.execute();
                        
                        listaPrueba.remove(tblprueba.getSelectionModel().getSelectedItem());
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                break;
                
            case GUARDAR:
                break;
        }
    }
    
    public void editar(){
        switch(tipodeoperacion){
            case NINGUNO:
                break;
                
            case ACTUALIZAR:
                break;
        }
    }
    
    public void actualizar(){
        Prueba registro =  (Prueba)tblprueba.getSelectionModel().getSelectedItem();
        registro.setNombre(txtnombre.getText());
        registro.setApellido(txtapellido.getText());
        registro.setEdad(Integer.parseInt(txtedad.getText()));
        
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Editar(?,?,?)}");
            procedimiento.setString(1, registro.getNombre());
            procedimiento.setString(2, registro.getApellido());
            procedimiento.setInt(3, registro.getEdad());
            procedimiento.execute();
        } catch (Exception e) {
        }
    }
    
    public void reporte(){
        switch(tipodeoperacion){
            case NINGUNO:
                break;
                
            case REPORTE:
                break;
        }
    }
    
    public void generarreporte(){
        Map parametros = new HashMap(); 
        parametros.put("nombre", null);
        GenerarReporte.mostrarReporte("Prueba.jasper","Prueba", parametros);
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    } 
    
    public void menuPrincipal(){
        principal.menuPrincipal();
    }
}
