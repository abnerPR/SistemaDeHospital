package org.abnerpiox.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.abnerpiox.bean.Medico;
import org.abnerpiox.db.Conexion;
import org.abnerpiox.report.GenerarReporte;
import org.abnerpiox.sistema.Principal;

public class MedicoController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico; // Creacion de la observableList

    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtTurnoMaximo;
    @FXML private TextField txtSexo;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        soloNumeros(txtLicenciaMedica);
        soloLetras(txtNombres);
        soloLetras(txtApellidos);
        horas(txtHoraEntrada);
        horas(txtHoraSalida);
        soloLetras(txtSexo);
        
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void soloNumeros(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                char ingresado = e.getCharacter().charAt(0);
                
                if(!Character.isDigit(ingresado) && ingresado != '\b' && ingresado != '\t'){
                    e.consume();
                    JOptionPane.showMessageDialog(null,"Solamente puede escribir Numeros");
                }else if(txtLicenciaMedica.getText().length() >= 9){
                    e.consume();
                }
            }
   });
                }
    
    public void soloLetras(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
               char ingresado = e.getCharacter().charAt(0);
               
               if(!Character.isLetter(ingresado) && !Character.isSpaceChar(ingresado) && ingresado != '\b' && ingresado != '\t'){
                   e.consume();
                   JOptionPane.showMessageDialog(null,"Solamente puede escribir letras");
               }
            }
            
        });
    }

    public void horas(TextField txt){
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent e) {
                
                
                char ingresado = e.getCharacter().charAt(0);
               
                if(Character.isDigit(ingresado)){
                    if(txt.getText().length() == 1){
                        txt.setText(txt.getText() + ingresado + ":");
                        e.consume();
                    }else if(txt.getText().length() == 3){
                        txt.setText(txt.getText() + ingresado);
                        e.consume();
                    }else if(txt.getText().length() == 4){
                        txt.setText(txt.getText() + ingresado + ":");
                        e.consume();
                    }else if(txt.getText().length() > 4 && txt.getText().length() <= 7){
                        txt.setText(txt.getText() + ingresado);
                        e.consume();
                    }
                }
                
                if(!Character.isDigit(ingresado) && ingresado != '\b' && ingresado != '\t'){
                    e.consume();
                    JOptionPane.showMessageDialog(null,"Solamente puede escribir numeros");
                }else if(txt.getText().length() > 7){
                    e.consume();
                
            }
            }
        });
    }
    public void cargarDatos() {
        tblMedicos.setItems(getMedicos());
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("licenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaSalida"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));

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

    public void seleccionarElemento() {
        if (tblMedicos.getSelectionModel().getSelectedItem() != null) {
            txtLicenciaMedica.setText(String.valueOf(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
            txtNombres.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
            txtHoraEntrada.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
            txtHoraSalida.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
            txtTurnoMaximo.setText(String.valueOf(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
            txtSexo.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
        } else {
            JOptionPane.showMessageDialog(null, "Registro no valido");
        }
    }

    public void nuevo() throws SQLException {
        switch (tipoDeOperacion) {
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

    public void guardar() throws SQLException {
        if(txtLicenciaMedica.getText().length() != 0 && txtNombres.getText().length() != 0 && txtApellidos.getText().length() != 0 && txtHoraEntrada.getText().length() != 0 && txtHoraSalida.getText().length() != 0 && txtSexo.getText().length() != 0){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(txtHoraEntrada.getText());
        registro.setHoraSalida(txtHoraSalida.getText());
        registro.setSexo(txtSexo.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico(?,?,?,?,?,?)}"); // Un signo de interrogacion por cada dato a ingresar
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getHoraEntrada());
            procedimiento.setString(5, registro.getHoraSalida());
            procedimiento.setString(6, registro.getSexo());
            procedimiento.execute();
            listaMedico.add(registro);
            
            limpiarControles();
            desactivarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
        } catch (MySQLIntegrityConstraintViolationException e) {
            if(1062 == e.getErrorCode()){
                JOptionPane.showMessageDialog(null, "Licencia Medica Existente");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }else 
         JOptionPane.showMessageDialog(null,"Error, faltan datos para ingresar");
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
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
                if (tblMedicos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedico(?)}");
                            procedimiento.setInt(1, ((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{
                        limpiarControles();                          
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
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

    public void actualizar() {
        try {
            
            Medico registro = (Medico) tblMedicos.getSelectionModel().getSelectedItem();
            registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setHoraEntrada(txtHoraEntrada.getText());
            registro.setHoraSalida(txtHoraSalida.getText());
            registro.setSexo(txtSexo.getText());
            
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedico(?,?,?,?,?,?,?)}");
            
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getLicenciaMedica());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getHoraEntrada());
            procedimiento.setString(6, registro.getHoraSalida());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblMedicos.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
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

    public void GenerarReporte() {
        switch (tipoDeOperacion) {
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
        GenerarReporte.mostrarReporte("ReporteMedicos.jasper","Reporte de Medicos", parametros);
    }

    public void desactivarControles() {
        txtLicenciaMedica.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtTurnoMaximo.setEditable(false);
        txtSexo.setEditable(false);
        tblMedicos.setDisable(false);
    }

    public void activarControles() {
        txtLicenciaMedica.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
        txtTurnoMaximo.setEditable(false);
        txtSexo.setEditable(true);
        tblMedicos.setDisable(true);
    }

    public void limpiarControles() {
        tblMedicos.getSelectionModel().clearSelection();
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        txtTurnoMaximo.setText("");
        txtSexo.setText("");
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void ventanaTelefonosMedico() {
        escenarioPrincipal.ventanaTelefonosMedico();
    }

}
