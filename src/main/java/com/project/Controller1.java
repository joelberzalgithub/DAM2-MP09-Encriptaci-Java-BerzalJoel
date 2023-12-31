package com.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller1 {

    @FXML
    private Button button0, button1;
    @FXML
    private TextField keyField, fileField, destinationField;

    private Key publicKey;

    private boolean isEncrypted;
    
    // Mètode per canviar la vista actual a la vista 0

    @FXML
    private void animateToView0(ActionEvent event) {
        UtilsViews.setViewAnimating("View0");
    }

    // Mètode per començar a encriptar un arxiu

    @FXML
    private void initializeEncryption(ActionEvent event) {

        String inputFile = "data/" + fileField.getText();
        String encryptedFile = "data/" + destinationField.getText();
        String keyFile = "data/" + keyField.getText() + ".pub";
        String clonedKeyFile = "data/" + keyField.getText() + ".key";

        try {
            // Comprovem si la clau pública ja existeix
            if (new File(keyFile).exists()) {
                publicKey = readPublicKey(keyFile); // Llegim la clau pública des de l'arxiu
            }
            else {
                publicKey = generateKeys(keyFile, clonedKeyFile); // Generem una clau pública i una altra privada
            }

            // Encriptem l'arxiu
            encryptFile(inputFile, encryptedFile, publicKey);

            // Informem al programa que l'arxiu s'ha encriptat correctament
            isEncrypted = true;

        } catch (Exception e) { isEncrypted = false; }

        // Comprovem si l'arxiu s'ha encriptat correctament
        
        if (!isEncrypted) {
            UtilsViews.setViewAnimating("View3"); // Canviem la vista actual a la vista 3
        }
        else {
            UtilsViews.setViewAnimating("View4"); // Canviem la vista actual a la vista 4
        }
    }

    // Mètode per generar claus públiques i privades

    private Key generateKeys(String keyFile, String clonedKeyFile) throws Exception {

        // Generem una clau pública fent servir l'algorisme AES
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // La clau pública pot ser de mida 128, 192 o 256 píxels
        SecretKey publicKey = keyGen.generateKey();

        // Codifiquem la clau pública en format Base64
        String encodedKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // Desem la clau pública a l'arxiu keyFile
        try (FileOutputStream keyFileStream = new FileOutputStream(keyFile)) {
            keyFileStream.write(encodedKey.getBytes());
        }

        // Desem una clau privada mitjançant una còpia de la clau pública
        try (FileOutputStream keyFileStream = new FileOutputStream(clonedKeyFile)) {
            keyFileStream.write(encodedKey.getBytes());
        }

        return publicKey;
    }
    
    // Mètode per llegir una clau pública des d'un arxiu

    private Key readPublicKey(String keyFile) throws Exception {

        FileInputStream keyFileStream = new FileInputStream(keyFile);
        byte[] encodedKey = keyFileStream.readAllBytes();
        keyFileStream.close();

        // Decodifiquem la clau pública des de Base64
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

        // Creem un objecte SecretKey a partir de la clau pública decodificada
        return new SecretKeySpec(decodedKey, "AES");
    }

    // Mètode per encriptar un arxiu

    private void encryptFile(String inputFile, String encryptedFile, Key privateKey) throws Exception {
        
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        
        FileInputStream inputStream = new FileInputStream(inputFile);
        CipherOutputStream outputStream = new CipherOutputStream(new FileOutputStream(encryptedFile), cipher);

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }
}
