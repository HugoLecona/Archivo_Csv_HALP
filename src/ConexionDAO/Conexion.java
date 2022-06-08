/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Datos.Datos;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lecona
 */
public class Conexion {
    
    Connection conexion;
    List<Datos> listaDatos = new ArrayList<Datos>();
    
    public void Abrir(){
       String user="root";
       String password="root";
       String url="jdbc:mysql://localhost:3306/excel_p?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
           conexion = DriverManager.getConnection(url, user ,password);
       } catch (ClassNotFoundException | SQLException ex){
           ex.printStackTrace();
       }
       
    }
    public void cerrar(){
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean insertar(Datos datos){
        boolean estado = true;
        
        try{
            Abrir();
            PreparedStatement ps = conexion.prepareStatement("insert into Datos(nombre, edad, sexo, correo, celular) values(?,?,?,?,?)");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.setString(4, datos.getCorreo());
            ps.setString(5, datos.getCelular());
            ps.execute();
            
        }catch(SQLException ex){
            ex.printStackTrace();
            estado = false;
        }finally{
            cerrar();
        }
        
        return estado;
        
    }
    
    
    public boolean actualizar(Datos datos){
         boolean estado = true;
        
        try{
            Abrir();
            PreparedStatement ps = conexion.prepareCall("update Datos set nombre = ?, edad = ?, sexo = ?, correo = ?, celular = ? where id = ?");
            ps.setString(1, datos.getNombre());
            ps.setString(2, datos.getEdad());
            ps.setString(3, datos.getSexo());
            ps.setString(4, datos.getCorreo());
            ps.setString(5, datos.getCelular());
            ps.executeUpdate();
            
            
        }catch(SQLException ex){
            ex.printStackTrace();
            estado = false;
        }finally{
            cerrar();
        }
        
        return estado;
    }
    
    public boolean consultartodos(){
        boolean estado = true;
        
        try{
            Abrir();
            PreparedStatement ps = conexion.prepareCall("select * from Datos");
            ResultSet rs = ps.executeQuery();
            Datos datos;
            
            while(rs.next()){
                //datos = new Datos(rs.getNString("nombre"), rs.getInt("edad"), rs.getNString("correo")); 
                datos = new Datos();
                datos.setId(rs.getInt("id"));
                datos.setNombre(rs.getNString("nombre")); //se puede de esta forma o de la de arriba
                datos.setEdad(rs.getString("edad"));
                datos.setSexo(rs.getNString("sexo"));
                datos.setCorreo(rs.getNString("correo"));
                datos.setCelular(rs.getString("celular"));
                listaDatos.add(datos);
            }
            
        }catch(SQLException ex){
            ex.printStackTrace();
            estado = false;
        }finally{
            cerrar();
        }
        
        return estado;
    }
    
    public List<Datos> getListDatos(){
        return listaDatos;
    }
    
    public boolean borrar(int id){
        boolean estado = true;
        try{
            Abrir();
            PreparedStatement ps = conexion.prepareStatement("delete form Datos where id = ?");
            ps.setInt(1, id);
            ps.execute();
        }catch(SQLException ex){
            ex.printStackTrace();
            estado = false;
        }finally{
            cerrar();
        }
        return estado;
    }
}
