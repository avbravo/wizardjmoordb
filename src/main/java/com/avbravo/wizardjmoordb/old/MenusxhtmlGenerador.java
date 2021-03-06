/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avbravo.wizardjmoordb.old;

import com.avbravo.wizardjmoordb.utilidades.JSFUtil;
import com.avbravo.wizardjmoordb.MySesion;
import com.avbravo.wizardjmoordb.ProyectoJEE;
import com.avbravo.wizardjmoordb.beans.EntidadMenu;
import com.avbravo.wizardjmoordb.utilidades.Utilidades;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author avbravoserver
 */
@Named
@RequestScoped
public class MenusxhtmlGenerador implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(MenusxhtmlGenerador.class.getName());

    @Inject
    MySesion mySesion;
    @Inject
    ProyectoJEE proyectoJEE;

    /**
     * Creates a new instance of Facade
     */
    public void generar() {
        try {

            mySesion.getMenubarList().stream().forEach((s) -> {
                procesar(s, "menu" + Utilidades.letterToLower(s) + ".xhtml", proyectoJEE.getPathMainWebapp() + proyectoJEE.getSeparator() + "menu" + Utilidades.letterToLower(s) + ".xhtml");
            });

        } catch (Exception e) {
            JSFUtil.addErrorMessage("generar() " + e.getLocalizedMessage());

        }
    }

    private Boolean procesar(String barramenu, String archivo, String ruta) {
        try {

            Path path = Paths.get(ruta);
            if (Files.notExists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                crearFile(barramenu, ruta, archivo);
            }

            for (EntidadMenu em : mySesion.getEntidadMenuList()) {
                
//                if (Utilidades.letterToLower(barramenu).equals(Utilidades.letterToLower(em.getMenu()))) {
//                    Utilidades.searchAdd(ruta, "<b:navLink value=\"#{men['menu." + Utilidades.letterToLower(em.getEntidad()) + "']}\"   rendered=\"#{menuBeans." + Utilidades.letterToLower(em.getEntidad()) + ".consultar}\" href=\"/faces/pages/" + Utilidades.letterToLower(em.getEntidad()) + "/" + Utilidades.letterToLower(em.getEntidad()) + ".xhtml\" />", "<b:dropMenu rendered=\"#{menuBeans.barra" + Utilidades.letterToUpper(barramenu) + "}\" value=\"#{men['menubar." + Utilidades.letterToLower(barramenu) + "']}\" >", Boolean.FALSE);
                    Utilidades.searchAddWithoutLine(ruta, "<b:navLink value=\"#{men['menu." + Utilidades.letterToLower(em.getEntidad()) + "']}\"   rendered=\"#{menuBeans." + Utilidades.letterToLower(em.getEntidad()) + ".consultar}\" href=\"/faces/pages/" + Utilidades.letterToLower(em.getEntidad()) + "/" + Utilidades.letterToLower(em.getEntidad()) + ".xhtml\" />", "</b:dropMenu>", Boolean.TRUE);

//                }

            }
            //las opciones de reportes
         
            if (barramenu.equals(mySesion.getOpcionMenuReportes())) {
         
                for (EntidadMenu em : mySesion.getEntidadMenuList()) {
                    Utilidades.searchAddWithoutLine(ruta, "<b:navLink value=\"#{men['menu." + Utilidades.letterToLower(em.getEntidad()) + "']}\"   rendered=\"#{menuBeans." + Utilidades.letterToLower(em.getEntidad()) + ".listar}\" href=\"/faces/pages/" + Utilidades.letterToLower(em.getEntidad()) + "/" + Utilidades.letterToLower(em.getEntidad()) + "reportes.xhtml\" />", "</b:dropMenu>", Boolean.TRUE);
                }

            }
        } catch (Exception e) {
            JSFUtil.addErrorMessage("procesar() " + e.getLocalizedMessage());
        }
        return true;

    }

    /**
     * deleteAll
     *
     * @param entidad
     * @param archivo
     * @return
     */
    private Boolean crearFile(String barraMenu, String path, String archivo) throws IOException {
        try {

            String ruta = path;
            File file = new File(ruta);
            BufferedWriter bw;
            if (file.exists()) {
                // El fichero ya existe
            } else {
                // El fichero no existe y hay que crearlo
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.close();
//      bw.write("Acabo de crear el fichero de texto.");

                File file2 = new File(ruta);
                //Creamos un objeto para escribir caracteres en el archivo de prueba
                try (FileWriter fw = new FileWriter(file)) {

                    fw.write("<html xmlns=\"http://www.w3.org/1999/xhtml\"" + "\r\n");
                    fw.write("      xmlns:ui=\"http://java.sun.com/jsf/facelets\"" + "\r\n");
                    fw.write("      xmlns:h=\"http://xmlns.jcp.org/jsf/html\"" + "\r\n");
                    fw.write("      xmlns:b=\"http://bootsfaces.net/ui\"" + "\r\n");
                    fw.write("      xmlns:p=\"http://primefaces.org/ui\">" + "\r\n");
                    fw.write("" + "\r\n");
                    fw.write("     <h:body>" + "\r\n");
                    fw.write("         <ui:composition>" + "\r\n");
                    fw.write("               <b:dropMenu rendered=\"#{menuBeans.barraMenu" + Utilidades.letterToUpper(barraMenu) + "}\" value=\"#{men['menubar." + Utilidades.letterToLower(barraMenu) + "']}\" >" + "\r\n");

                    for (EntidadMenu em : mySesion.getEntidadMenuList()) {
                        if (Utilidades.letterToLower(barraMenu).equals(Utilidades.letterToLower(em.getMenu()))) {
                            fw.write("                <b:navLink value=\"#{men['menu." + Utilidades.letterToLower(em.getEntidad()) + "']}\"   rendered=\"#{menuBeans." + Utilidades.letterToLower(em.getEntidad()) + ".consultar}\" href=\"/faces/pages/" + Utilidades.letterToLower(em.getEntidad()) + "/" + Utilidades.letterToLower(em.getEntidad()) + ".xhtml\" />");
                            fw.write("\r\n");
                        }

                    }
                    fw.write("               </b:dropMenu>" + "\r\n");
                    fw.write("        </ui:composition>" + "\r\n");
                    fw.write("     </h:body>" + "\r\n");
                    fw.write("</html>" + "\r\n");
                    fw.close();

                } catch (IOException ex) {
                    JSFUtil.addErrorMessage("crearFile() " + ex.getLocalizedMessage());
                }

            }
        } catch (Exception e) {
            JSFUtil.addErrorMessage("crearFile() " + e.getLocalizedMessage());
        }
        return false;
    }

    private String username() {
        try {

            String texto = "";
            texto += "                <div class=\"form-group\">" + "\r\n";
            texto += "                    <b:inputText rendered=\"#{!loginBean.logeado}\" value=\"#{loginBean." + Utilidades.letterToLower(mySesion.getEntidadUser().getTabla()) + "." + Utilidades.letterToLower(mySesion.getAtributosUsername()) + "}\" placeholder=\"#{app['login.username']}\" fieldSize=\"sm\"/>" + "\r\n";
            texto += "                </div>" + "\r\n";
            return texto;
        } catch (Exception e) {
            JSFUtil.addErrorMessage("username()  " + e.getLocalizedMessage());
        }
        return "";
    }

    private String password() {
        try {

            String texto = "";
            texto += "                <div class=\"form-group\">" + "\r\n";
            texto += "<p:password rendered=\"#{!loginBean.logeado}\" value=\"#{loginBean." + Utilidades.letterToLower(mySesion.getEntidadUser().getTabla()) + "." + Utilidades.letterToLower(mySesion.getAtributosPassword()) + "}\" placeholder=\"#{app['login.password']}\" />" + "\r\n";
            texto += "                </div>" + "\r\n";
            return texto;
        } catch (Exception e) {
            JSFUtil.addErrorMessage("password()  " + e.getLocalizedMessage());
        }
        return "";
    }

}
