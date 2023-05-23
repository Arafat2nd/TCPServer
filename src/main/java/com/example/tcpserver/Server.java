package com.example.tcpserver;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {
    @FXML
    private ComboBox<?> combobox;

    @FXML
    private TextField portnum;

    @FXML
    private Button startListening;

    @FXML
    private Label status;

    @FXML
    private TextArea users;

    int port = 0;

    @FXML
    void startLesteningAction(MouseEvent event) throws SQLException, IOException {
        port = Integer.parseInt(portnum.getText());
        startListening.setDisable(true);
        status.setText("Listeneing at port " + port + " and ip 127.0.0.1");
        ServerSocket welcomeSocket;
        boolean flag = true;
        if (flag && port != 0) {
            flag = false;
            welcomeSocket = new ServerSocket(port);
            Thread receiver = new Thread(() -> {
                while (true) {
                    try {
                        users.setText(Persistent.fetchActive().replace("^","\n"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    Socket connectionSocket = null;
                    try {
                        connectionSocket = welcomeSocket.accept();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    BufferedReader inFromClient =
                            null;
                    try {
                        inFromClient = new BufferedReader(new
                                InputStreamReader(connectionSocket.getInputStream()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    DataOutputStream outToClient =
                            null;
                    try {
                        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String user = null;
                    try {
                        user = inFromClient.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (user.split("GG")[0].split("@")[1].equals("login")) {
                        try {
                            if (Persistent.loginUser(user
                                    .split("GG")[0].split("@")[0].split("-")[0], user.split("GG")[0].split("@")[0].split("-")[1])) {
                                try {
                                    Persistent.addActive(user
                                            .split("GG")[1] + "==" + user
                                            .split("GG")[0].split("@")[0].split("-")[0]);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    outToClient.writeBytes("success" + "\n");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    connectionSocket.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            } else {
                                try {
                                    outToClient.writeBytes("fail" + "\n");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    connectionSocket.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        if (user.split("@")[1].equals("remove")) {
                            System.out.println(user);
                            try {
                                Persistent.removeUserFromActive(user.split("@")[0]);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                outToClient.writeBytes("done" + "\n");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                connectionSocket.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        } else {
                            if (user.equals("active@active")) {
                                String active = null;
                                try {
                                    active = Persistent.fetchActive();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.println(active);
                                try {
                                    outToClient.writeBytes(active + "\n");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    connectionSocket.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }

                    }
                    try {
                        connectionSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }


            }
            );
            receiver.start();


        } else {
            welcomeSocket = null;
        }



    }
}

