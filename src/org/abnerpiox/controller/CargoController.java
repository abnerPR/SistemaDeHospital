
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
import org.abnerpiox.bean.Cargo;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;


public class CargoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    
    @FXML private TextField txtNombreCargo;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colCargos;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        soloLetras(txtNombreCargo);
    }
    
    public void soloLetras(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                char ingresado = e.getCharacter().charAt(0);
                
            if(!Character.isLetter(ingresado) && !Character.isSpaceChar(ingresado) && ingresado != '\b'){
                e.consume();
                JOptionPane.showMessageDialog(null,"Solamente puede escribir letras");
            }
            }    
        });  
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargos());
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colCargos.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));    
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
    
    public void selecionarElemento(){
        if(tblCargos.getSelectionModel().getSelectedItem() != null){
        txtNombreCargo.setText(((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo());
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
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Desea eliminar el registro?","Eliminar Area",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                            procedimiento.setInt(1, ((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                            procedimiento.execute();
                            listaCargo.remove(tblCargos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        limpiarControles();
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
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
      
      public void actualizar(){
          try{
              PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargo(?,?)}");
              Cargo registro = (Cargo)tblCargos.getSelectionModel().getSelectedItem();
              registro.setNombreCargo(txtNombreCargo.getText());
              procedimiento.setInt(1, registro.getCodigoCargo());
              procedimiento.setString(2, registro.getNombreCargo());
              procedimiento.execute();
          }catch(Exception e){
              e.printStackTrace();
          }
      }
      
      public void editar(){
          switch(tipoDeOperacion){
              case NINGUNO:
                  if(tblCargos.getSelectionModel().getSelectedItem() != null){
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
          }
      }
      
      public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoMedico",null);
        GenerarReporte.mostrarReporte("ReporteCargos.jasper","Reporte de Cargos", parametros);
      }
      
      public void guardar(){   
       if(txtNombreCargo.getText().length() != 0){
       Cargo registro = new Cargo();
       registro.setNombreCargo(txtNombreCargo.getText());
   
       try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{Call sp_AgregarCargo(?)}"); // Un signo de interrogacion por cada dato a ingresar
           procedimiento.setString(1, registro.getNombreCargo());
           procedimiento.execute();
           listaCargo.add(registro);
           
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
        txtNombreCargo.setEditable(false);
        tblCargos.setDisable(false);
    }

    public void activarControles(){
        txtNombreCargo.setEditable(true);
        tblCargos.setDisable(true);
    }
    
    public void limpiarControles(){
        txtNombreCargo.setText("");
        tblCargos.getSelectionModel().clearSelection();
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
