package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.SystemLib;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbWorker implements DbWorkerItf {

    private Connection dbConnexion;
    private List<Personne> listePersonnes;
    private int index = 0;

    /**
     * Constructeur du worker
     */
    public DbWorker() {
    }

    @Override
    public void connecterBdMySQL(String nomDB) throws MyDBException {
        final String url_local = "jdbc:mysql://localhost:3306/" + nomDB;
        final String url_remote = "jdbc:mysql://LAPEMFB37-21.edu.net.fr.ch:3306/" + nomDB;
        final String user = "root";
        final String password = "emf123";

        System.out.println("url:" + url_local);
        try {
            dbConnexion = DriverManager.getConnection(url_local, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdHSQLDB(String nomDB) throws MyDBException {
        final String url = "jdbc:hsqldb:file:" + nomDB + ";shutdown=true";
        final String user = "SA";
        final String password = "";
        System.out.println("url:" + url);
        try {
            dbConnexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdAccess(String nomDB) throws MyDBException {
        final String url = "jdbc:ucanaccess://" + nomDB;
        System.out.println("url=" + url);
        try {
            dbConnexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void deconnecter() throws MyDBException {
        try {
            if (dbConnexion != null) {
                dbConnexion.close();
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    public List<Personne> lirePersonnes() throws MyDBException {
        listePersonnes = new ArrayList<Personne>();
        try {
            Statement st = dbConnexion.createStatement();
            ResultSet rs = st.executeQuery("select Nom, Prenom from t_personne");
            while (rs.next()) {
                Personne current = new Personne(rs.getString("Nom"), rs.getString("Prenom"));
                listePersonnes.add(current);
            }

        } catch (SQLException e) {
        }

        return listePersonnes;
    }

    @Override
    public Personne precedentPersonne() throws MyDBException {
        List<Personne> pers = lirePersonnes();
        try {
            if (index > 0) {
                index -= 1;
                return pers.get(index);
            } else {
                return pers.get(index);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Personne suivantPersonne() throws MyDBException {
        List<Personne> pers = lirePersonnes();
        try {
                if (index < pers.size()-1) {
                    index += 1;
                    return pers.get(index);

                } else {
                    return pers.get(index);
                
            }
        } catch (Exception e) {
        }
        return null;
    }

}
