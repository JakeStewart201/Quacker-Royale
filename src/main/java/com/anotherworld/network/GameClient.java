package com.anotherworld.network;

import com.anotherworld.tools.datapool.*;
import com.anotherworld.tools.input.Input;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class allows clients to get game objects from server and send the key presses to the server
 *
 * @author Antons Lebedjko
 */
public class GameClient extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private int port = 4445;
    private int numberOfInitialObjects = 5;
    private ArrayList<BallData> ballData = null;
    private ArrayList<PlayerData> playerData = null;
    private GameSessionData gameSessionData = null;
    private PlatformData platformData = null;
    private WallData wallData = null;
    private PlayerData clientPlayer = null;
    private String myID;
    private static Logger logger = LogManager.getLogger(GameClient.class);
    private boolean stopClient = false;

    /**
     * Used to set up a connection with the server and wait until host informs that game can start
     *
     * @param serverIP The ip address of the host player, to which clients should connect
     */
    public GameClient(String serverIP) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(serverIP);
        logger.trace("Client ip: " + Inet4Address.getLocalHost().getHostAddress());
        sendDataToServer("set up connection message");
        waitForGameToStart();
    }

    /**
     * A run method for the thread which receives the objects from the host player
     */
    public void run() {
        getInitialObjectsOfTheGame();
        sendDataToServer("Initial objects have been received. Let's start!!!!");
        while(!stopClient) {
            try {
                getObjectFromServer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Used to send strings to the host player
     *
     * @param msg Any message in a String format
     */
    public void sendDataToServer(String msg) {
        byte[] data = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to send the key presses of the client
     *
     * @param input ArrayList of the buttons pressed on a keyboard
     */
    public void sendKeyPresses(ArrayList<Input> input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(input);
        byte[] data = outputStream.toByteArray();
        DatagramPacket packet
                = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.trace("Key press has been send to the server");
    }

    /**
     * Waits until server informs game can be started. Also, client receives his ID
     */
    public void waitForGameToStart() {
        byte[] data = new byte[16];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(packet.getData(), 0, packet.getLength());
        myID = received;
        logger.trace("My id is:" + received);
    }

    /**
     * Method which receives all the possible objects from a host player
     */
    public void getObjectFromServer() throws ClassNotFoundException, IOException {
        byte incomingData[] = new byte[1024];
        DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
        try {
            socket.receive(incomingPacket);
        } catch (IOException e) {
            logger.error("Client socket has been closed.");
        }
        byte data[] = incomingPacket.getData();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        Object object = objectInputStream.readObject();
        if (object instanceof ArrayList<?>) {
            logger.trace("ArrayList has been received");
            ArrayList<?> ballOrPlayer = ((ArrayList<?>)object);
            if (ballOrPlayer.get(0) instanceof PlayerData) {
                playerData = (ArrayList<PlayerData>) ballOrPlayer;
                logger.trace("Player data object has been received");
            } else if (ballOrPlayer.get(0) instanceof BallData) {
                ballData = (ArrayList<BallData>) ballOrPlayer;
                logger.trace("Ball object received.");
            }
        } else if (object instanceof GameSessionData) {
            gameSessionData = (GameSessionData) object;
            logger.trace("GameSessionData object has been received");
        } else if (object instanceof PlatformData) {
            platformData = (PlatformData) object;
            logger.trace("PlatformData object has been received");
        } else if (object instanceof WallData) {
            wallData = (WallData) object;
            logger.trace("WallData object has been received");
        }
    }

    /**
     * Waits to receive initial objects of the game from a host player
     */
    public void getInitialObjectsOfTheGame(){
        try {
            for(int i = 0; i < numberOfInitialObjects; i++){
                getObjectFromServer();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the BallData object
     */
    public ArrayList<BallData> getBallData() {
        return this.ballData;
    }

    /**
     * @return the PlayerData object
     */
    public ArrayList<PlayerData> getPlayerData() {
        return this.playerData;
    }

    /**
     * @return the GameSessionData object
     */
    public GameSessionData getGameSessionData() {
        return this.gameSessionData;
    }

    /**
     * @return the PlatformData object
     */
    public PlatformData getPlatformData() {
        return this.platformData;
    }

    /**
     * @return the WallData object
     */
    public WallData getWallData() {
        return this.wallData;
    }

    /**
     * @return the PlayerData of a client
     */
    public PlayerData getClientPlayer() {
        if (playerData != null) {
            for (int i = 0; i < playerData.size(); i++) {
                if (playerData.get(i).getObjectID().equals(myID)) {
                    clientPlayer = playerData.get(i);
                    return  clientPlayer;
                }
            }
        }
        return this.clientPlayer;
    }

    /**
     * Stops the communication with server and closes the socket
     */
    public void stopClient() {
        stopClient = true;
        socket.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        GameClient client = new GameClient("localhost");
        client.start();
        int counter = 0;
        while (true) {
            ArrayList<Input> keyPresses = new ArrayList<>();
            keyPresses.add(Input.LEFT);
            client.sendKeyPresses(keyPresses);
        }
    }
}
