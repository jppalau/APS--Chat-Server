/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ZF676VA
 */
public class Chat implements Runnable{
    public static final String SERVER_ADDRESS = "127.0.0.1";
    
    private ClientSocket clientSocket;
    public String msg;
    public static String nome = "joao";
    
    
    public static void main(String[] args) {
        try {
            Chat chat = new Chat();
            chat.start();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }     
    }
    
    /*public Chat(){
        scr = new Scanner(System.in);
    }*/
    
    public void start() throws IOException{
        final Socket socket = new Socket(SERVER_ADDRESS, Server.PORT);
        clientSocket = new ClientSocket(socket, nome);
        System.out.println("Cliente conectado ao servidor no endereço " + SERVER_ADDRESS + " e porta " + Server.PORT);
        System.out.println("Digite uma msg (ou 'sair' para encerrar): ");
        
        Thread thread = new Thread(this);
        thread.start();
        
        messageLoop();
        
    }
    
    @Override
    public void run() {
        while((msg = clientSocket.getMessage())!= null) {
            System.out.println(msg);
        }
    }
    
    public void messageLoop(){
        do {     
            Scanner scr = new Scanner(System.in);
            msg = scr.nextLine();
            clientSocket.sendMsg(msg);
        } while (!"sair".equalsIgnoreCase(msg));
            clientSocket.close();

    }
    
    
    
}
