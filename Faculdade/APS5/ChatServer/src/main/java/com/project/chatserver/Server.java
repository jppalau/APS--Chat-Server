/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ZF676VA
 */
public class Server {
    public static final int PORT = 5000;
    
    private ServerSocket serverSocket;
    
    private final List<ClientSocket> clientSocketList;
    ArrayList  arrayName = new ArrayList<String>();  
    ArrayList<Integer> arrayCountName = new ArrayList<Integer>();
    
    public Server() {
        clientSocketList = new LinkedList<>();
    }
    
    public static void main(String[] args) throws IOException{
        Server server = new Server();
        server.start();
    }
    
    private void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println(
                "Servidor de chat bloqueante iniciado no endereço " + serverSocket.getInetAddress().getHostAddress() +
                " e porta " + PORT);

        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        try {
            while (true) {
                System.out.println("Aguardando conexão de novo cliente");
                
                ClientSocket clientSocket;
                try {
                    clientSocket = new ClientSocket(serverSocket.accept());
                    System.out.println("Cliente " +  clientSocket.getAddress() + " conectado");
                }catch(SocketException e){
                    System.err.println("Erro ao aceitar conexão do cliente. O servidor possivelmente está sobrecarregado:");
                    System.err.println(e.getMessage());
                    continue;
                }
                /*
                Cria uma nova Thread para permitir que o servidor não fique bloqueado enquanto
                atende às requisições de um único cliente.
                */
                try {
                    new Thread(() -> clientMessageLoop(clientSocket)).start();
                    clientSocketList.add(clientSocket);
                    arrayCountName.add(0);
                }catch(OutOfMemoryError ex){
                    System.err.println(
                            "Não foi possível criar thread para novo cliente. O servidor possivelmente está sobrecarregdo. Conexão será fechada: ");
                    System.err.println(ex.getMessage());
                    clientSocket.close();

                }
            }
        } finally{
            /*Se sair do laço de repetição por algum erro, exibe uma mensagem
            indicando que o servidor finalizou e fecha o socket do servidor.*/
            stop();
        }
        
    }
    
    private void clientMessageLoop(final ClientSocket clientSocket){
        try {
            String msg;
            while((msg = clientSocket.getMessage()) != null){           
                try {
                    for (int i = 0; 0 < clientSocketList.size(); i++) {
                        if (clientSocketList.get(i) == clientSocket) {
                            if (arrayCountName.get(i) == 0) {
                                System.out.println("esse é o index " + i);
                                arrayName.add(i, msg);
                                System.out.println("nome: " + msg);
                                arrayCountName.set(i, 1);
                            }else{
                                System.out.println("Mensagem recebida do cliente " + arrayCountName.get(i) + " | " +  clientSocket.getAddress() +": " + msg);
                                sendMsgToAll(clientSocket, msg);
                            }
                        }
                    }
                } catch (Exception e) {
                }
                //System.out.println(clientSocketList.indexOf(clientSocket.getAddress())); 
                //System.out.println(arrayName.get(0));
                //System.out.println("Mensagem recebida do cliente " +  clientSocket.getAddress() +": " + msg);

                if("sair".equalsIgnoreCase(msg)){
                    return;
                }
                
            }
        } finally {
            clientSocket.close();
        }
    }
    
    private void sendMsgToAll(final ClientSocket sender, final String msg) {
        final Iterator<ClientSocket> iterator = clientSocketList.iterator();
        int count = 0;
        int getIndex = arrayName.indexOf("joao");
        int getIndex2 = clientSocketList.indexOf(sender);
        while (iterator.hasNext()) {
            final ClientSocket client = iterator.next();
            
            if (client.equals(clientSocketList.get(getIndex))) {
                
                client.sendMsg(sender.getAddress() + " | " + arrayName.get(clientSocketList.indexOf(sender)) + ": " + msg); 
                count++;
               //iterator.remove();
            }
        }
        System.out.println("Mensagem encaminhada para " + count + " clientes");
    }
    
    public void stop(){
        try {
            System.out.println("Finalizando o servidor");
            serverSocket.close();
        } catch (Exception e) {
            
        }        
    }
}
