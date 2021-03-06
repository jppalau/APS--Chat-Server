/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.util.Arrays;

/**
 *
 * @author ZF676VA
 */
public class ClientSocket {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    
    public ClientSocket(final Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public boolean sendMsg(String msg) { //retorna true se não houve nenhum erro ao enviar mensagem ou false caso tenha havido
        out.println(msg);
       
        return !out.checkError();
    }
    
    public String getMessage() {  // recebe a mensagem do outro usuario
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    
    public void close(){  // fecha o socket?
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
        }       
    }
    
    public SocketAddress getAddress(){
        return socket.getRemoteSocketAddress();
    }

    /**
     * @return the nome
     */

    /*public boolean isOpen(){
        return !socket.isClosed();
    }*/

    /**
     * @return the name
     */
   /* public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    /*public void setName(String name) {
        this.name = name;
    }*/
}
