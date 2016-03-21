/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.khmt.restful;

import java.io.IOException;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Phuo
 */
public class AuthenticationService {
    public boolean GetUserAuthenticate(String authString, String getID, ConnectToSQL1 dbConnection){
        String[] userPassword = decode(authString);
        String[] result = dbConnection.checkUser(userPassword[0], userPassword[1]);
        if (!result[0].equals(ConnectToSQL1.ERROR) && !result[0].equals(ConnectToSQL1.NOTMATCH)){
            if (result[1].equals("3")){
                return true;
            } else {
                return result[0].equals(getID);
            }
        } else {
            return false;
        }
    }
    private String[] decode (String authString){
        String decodedAuth = "";
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
        System.out.println(decodedAuth);
        String username = decodedAuth.split(":")[0];
        String password = decodedAuth.split(":")[1];
        return new String[]{username, password};
    }
    public boolean GetAllAuthenticate(String authString, ConnectToSQL1 dbConnection){
        String[] userPass = decode(authString);
        String[] result = dbConnection.checkUser(userPass[0], userPass[1]);
        return result[1].equals("3");
    }
}
