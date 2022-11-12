package com.example.cloudapplication_2022;

import com.geekbrains.cloud2022.CloudMessage;
import io.netty.handler.codec.serialization.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
//network
@Slf4j
public class Network {
    private final ObjectDecoderInputStream is;
    private final ObjectEncoderOutputStream os;

    public Network(int port) throws IOException {
        Socket socket = new Socket("localhost", port);
        os = new ObjectEncoderOutputStream(socket.getOutputStream());
        is = new ObjectDecoderInputStream(socket.getInputStream());
    }

    public CloudMessage read() throws IOException, ClassNotFoundException {
        return (CloudMessage) is.readObject();
    }

    public void write(CloudMessage msg) throws IOException {
        os.writeObject(msg);
        os.flush();
    }
}
