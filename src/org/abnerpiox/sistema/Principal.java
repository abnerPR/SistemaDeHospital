
package org.abnerpiox.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.abnerpiox.controller.AreaController;
import org.abnerpiox.controller.CargoController;
import org.abnerpiox.controller.ContactoUrgenciaController;
import org.abnerpiox.controller.EspecialidadController;
import org.abnerpiox.controller.HorariosController;
import org.abnerpiox.controller.MedicoController;
import org.abnerpiox.controller.MedicoEspecialidadController;
import org.abnerpiox.controller.MenuPrincipalController;
import org.abnerpiox.controller.PacienteController;
import org.abnerpiox.controller.ProgramadorController;
import org.abnerpiox.controller.ResponsableTurnoController;
import org.abnerpiox.controller.TelefonosMedicoController;
import org.abnerpiox.controller.TurnoController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/abnerpiox/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
     
    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        escenarioPrincipal.getIcons().add(new Image("org/abnerpiox/images/hospitalicono.png"));
        menuPrincipal();
        escenarioPrincipal.show();
        
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",600,341);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaMedicos(){
        try{
            MedicoController medicoController = (MedicoController)cambiarEscena("MedicoView.fxml",818,526);
            medicoController.setEscenarioPrincipal(this); //Pasar el medicoController al escenario Principal
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
            try{
                ProgramadorController programadorController = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",491,344);
                programadorController.setEscenarioPrincipal(this);
            }catch(Exception e){
            e.printStackTrace();
       }
    }
    
    public void ventanaTelefonosMedico(){
            try{
                TelefonosMedicoController telefonosMedico = (TelefonosMedicoController)cambiarEscena("TelefonosMedicoView.fxml",556,431);
                telefonosMedico.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void ventanaPacientes(){
            try{
                PacienteController pacienteController = (PacienteController)cambiarEscena("PacientesView.fxml",934,518);
                pacienteController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
    }

    public void ventanaContactoUrgencia(){
            try{
                ContactoUrgenciaController contactoUrgenciaController = (ContactoUrgenciaController)cambiarEscena("ContactoUrgenciaView.fxml",609,424);
                contactoUrgenciaController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void ventanaEspecialidades(){
            try{
                EspecialidadController especialidadController = (EspecialidadController)cambiarEscena("EspecialidadesView.fxml",509,400);
                especialidadController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void ventanaAreas(){
            try{
                AreaController areaController = (AreaController)cambiarEscena("AreasView.fxml",531,400);
                areaController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void ventanaCargos(){
            try{
                CargoController cargoController = (CargoController)cambiarEscena("CargosView.fxml",499,400);
                cargoController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void ventanaHorarios(){
        try{
            HorariosController horarioController = (HorariosController)cambiarEscena("HorariosView.fxml",606,460);
            horarioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaResponsableTurno(){
        try{
            ResponsableTurnoController responsableTurno = (ResponsableTurnoController)cambiarEscena("ResponsableTurnoView.fxml",644,488);
            responsableTurno.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicoEspecialidad(){
        try{
            MedicoEspecialidadController medicoEspecialidadController = (MedicoEspecialidadController)cambiarEscena("MedicoEspecialidadView.fxml",624,384);
            medicoEspecialidadController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTurno(){
        try{
            TurnoController turnoController = (TurnoController)cambiarEscena("TurnoView.fxml",737,446);
            turnoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    
}
