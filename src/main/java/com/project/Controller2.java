package com.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

public class Controller2 {

    @FXML
    private Button button0, button1;
    @FXML
    private TextField fileField, keyField, passwordField, destinationField;

    private boolean isDecrypted;

    // Mètode per canviar la vista actual a la vista 0

    @FXML
    private void animateToView0(ActionEvent event) {
        UtilsViews.setViewAnimating("View0");
    }

    // Mètode per començar a desencriptar un arxiu
    
    @FXML
    private void initializeDecryption(ActionEvent event) {

        String encryptedFile = "data/" + fileField.getText();
        String keyFile = "data/" + keyField.getText();
        String keyPassword = passwordField.getText();
        String decryptedFile = "data/" + destinationField.getText();

        try {

            // Comprovem que l'arxiu d'entrada i el de destí són diferents

            if (!encryptedFile.equals(decryptedFile)) {

                // Comprovem que la clau introduïda sigui privada

                if (keyFile.endsWith(".key")) {

                    // Comprovem si la clau privada ja existeix

                    if (new File(keyFile).exists()) {

                        // Comprovem si el contingut de la clau coincideix amb la contrasenya introduïda

                        if (hasValidPassword(keyFile, keyPassword)) {

                            // Llegim la clau privada des de l'arxiu
                            Key privateKey = readPrivateKey(keyFile);

                            // Desencriptem l'arxiu
                            decryptFile(encryptedFile, decryptedFile, privateKey);

                            // Informem al programa que l'arxiu s'ha desencriptat correctament
                            isDecrypted = true;

                        } else { isDecrypted = false; }

                    } else { isDecrypted = false; }
                    
                } else { isDecrypted = false; }
                
            } else { isDecrypted = false; }

        } catch (Exception e) { isDecrypted = false; }

        // Comprovem si l'arxiu s'ha desencriptat correctament
        
        if (!isDecrypted) {
            UtilsViews.setViewAnimating("View5"); // Canviem la vista actual a la vista 5
        }
        else {
            UtilsViews.setViewAnimating("View6"); // Canviem la vista actual a la vista 6
        }
    }

    // Mètode per validar la contrasenya d'una clau privada

    private boolean hasValidPassword(String keyFile, String keyPassword) throws Exception {

        FileInputStream keyFileStream = new FileInputStream(keyFile);
        byte[] encodedKey = keyFileStream.readAllBytes();
        keyFileStream.close();

        // Convertim la llista de bytes en un String
        String keyString = new String(encodedKey, StandardCharsets.UTF_8);

        // Comprovem si el contingut de la clau coincideix amb la contrasenya introduïda
        boolean isValid;
        if (keyString.equals(keyPassword)) {
            isValid = true;
        }
        else {
            isValid = false;
        }
        return isValid;
    }

    // Mètode per llegir una clau privada
    
    private Key readPrivateKey(String keyFile) throws Exception {

        FileInputStream keyFileStream = new FileInputStream(keyFile);
        byte[] encodedKey = keyFileStream.readAllBytes();
        keyFileStream.close();

        // Decodifiquem la clau privada des de Base64
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

        // Creem un objecte SecretKey a partir de la clau pública decodificada
        return new SecretKeySpec(decodedKey, "AES");
    }

    // Mètode per desencriptar un arxiu

    private void decryptFile(String encryptedFile, String decryptedFile, Key privateKey) throws Exception {

        try (FileInputStream inputStream = new FileInputStream(encryptedFile);
             FileOutputStream outputStream = new FileOutputStream(decryptedFile)) {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error decrypting file", e);
        }
    }
}
