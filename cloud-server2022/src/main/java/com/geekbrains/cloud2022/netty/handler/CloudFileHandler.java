package com.geekbrains.cloud2022.netty.handler;


import authorization2022.DbAuthService;
import com.geekbrains.cloud2022.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {
    private static final List<Channel> channels = new ArrayList<>();
    private static final Map<Path, User> map = new ConcurrentHashMap<>();
    private static final List<User> list = new ArrayList<>();
    private static int newClientIndex = 1;
    private final Path currentDir = Path.of("server_files");

    private final Path changeCurDirSerFolder = Path.of("specific_server_folder");
    private final Path clientDir = Path.of("specific_client_folder");
    private String fileDest = "";
    private final DbAuthService dbService;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    public CloudFileHandler() {
        dbService = new DbAuthService();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ListFiles(currentDir));
        ctx.writeAndFlush(new ListFiles(clientDir));
        ctx.writeAndFlush(new ListFiles(changeCurDirSerFolder));

        System.out.println("Client connected: " + ctx.channel());
        log.debug("client connected" + ctx.channel());
        channels.add(ctx.channel());
        newClientIndex++;
        channels.add(ctx.channel());

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        if (cloudMessage instanceof FileRequest fileRequest) {
            String result = fileRequest.getName();
            log.debug(result + "fileRequest");
            log.debug(fileDest + " " + "fileDest ");
            Path of = Path.of(result);
            if (Files.exists(of)) {
                if (Files.isDirectory(of)) {
                    log.debug(result + " " + "is directory");
                } else {
                    System.out.println(result + " " + " is file");
                    Path pathDest = Path.of(fileDest);
                    Files.copy(of, pathDest, StandardCopyOption.REPLACE_EXISTING);
                    log.debug(of + " " + "pathSource");
                    if (fileRequest.getName().contains(changeCurDirSerFolder.toString())) {
                        System.out.println("contains server_files (file request name )");
                        ctx.writeAndFlush(new ListFiles(Path.of(String.valueOf(clientDir))));
                    } else if (fileRequest.getName().contains(clientDir.toString())) {
                        ctx.writeAndFlush(new ListFiles(Path.of(String.valueOf(currentDir))));
                        ctx.writeAndFlush(new ListFiles(Path.of(String.valueOf(changeCurDirSerFolder))));
                    }
                }
            } else {
                log.debug("not exist");
            }
        } else if (cloudMessage instanceof FileMessage fileMessage) {
            if (fileMessage.getName().contains(changeCurDirSerFolder.toString())) {
                Files.write(changeCurDirSerFolder.resolve(fileMessage.getName()), fileMessage.getData());
                ctx.writeAndFlush(new ListFiles(changeCurDirSerFolder));
                System.out.println("contains curentdir in filemesage");
            } else {
                Files.write(clientDir.resolve(fileMessage.getName()), fileMessage.getData());
                ctx.writeAndFlush(new ListFiles(clientDir));
                System.out.println("not contains curentdir in filemesage");
            }
        } else if (cloudMessage instanceof CurrentDirectorForSaveFile currentDirectorForSaveFile) {
            fileDest = currentDirectorForSaveFile.getName();
            System.out.println("fileDest" + fileDest);
        } else if (cloudMessage instanceof AuthRequest authRequest) {
            System.out.println(authRequest.name + " " + "login" + " " + "password" + " " + authRequest.password);
            dbService.connection();
            String nickNameByLoginAndPassword = dbService.getNickNameByLoginAndPassword(authRequest.name, authRequest.password);
            System.out.println(nickNameByLoginAndPassword + " " + "nickNameByLoginAndPassword ");
            if (nickNameByLoginAndPassword != null) {
                String login = dbService.getNickNameByLoginAndPassword(authRequest.name, authRequest.password);
                this.dbService.getUser(login);
                System.out.println(this.dbService.getUser(login) + "  dbService.getUser(this.login);");
                log.info(login + " " + " login");
                Path path = Paths.get("specific_client_folder", login);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                    log.info("Directory created");
                    ctx.writeAndFlush(new ForNickName(login));
                } else {
                    log.info("Directory already exists");
                    ctx.writeAndFlush(new ForNickName(login));
                }
                Path server = Paths.get("specific_server_folder", login);
                if (Files.notExists(server)) {
                    try {
                        File src = new File(String.valueOf(currentDir));
                        File dst = new File(String.valueOf(server));
                        FileUtils.copyDirectory(src, dst);
                        String servSend = String.valueOf(server);
                        ctx.writeAndFlush(new ServerNameFolder(servSend));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.info("not execute ");
                }
                String send = String.valueOf(path);
                String servSend = String.valueOf(server);
                if (map.containsValue(this.dbService.getUser(login))) {
                    System.out.println("exist");
                    System.out.println(map.containsValue(this.dbService.getUser(login)) + "if containsValue");
                    ctx.writeAndFlush(new AuthSuccess(false));
                    ctx.writeAndFlush(new CheckIfOnline(true));
                } else {
                    map.put(server, this.dbService.getUser(login));
                    System.out.println("not");
                    System.out.println(map.containsValue(this.dbService.getUser(login)) + "else containsValue");
                    ctx.writeAndFlush(new AuthSuccess(true));
                    ctx.writeAndFlush(new SendName(send));
                    ctx.writeAndFlush(new ServerNameFolder(servSend));
                    list.add(this.dbService.getUser(login));

                    Path newFilePath = Paths.get("userfiles", nickNameByLoginAndPassword);
                    Path userfiles = Path.of("userfiles");
                    if (Files.notExists(newFilePath)) {
                        Files.createFile(newFilePath);
                        ctx.writeAndFlush(new ListUsers(userfiles));
                    } else {
                        ctx.writeAndFlush(new ListUsers(userfiles));
                    }
                    for (User user : list) {
                        log.info(user + "list users");
                    }
                }
                for (User value : map.values()) {
                    System.out.println("value mapka " + value.toString());
                }
                log.info(list + " list");
            } else {
                log.info("not exist" + " " + null);
                ctx.writeAndFlush(new AuthSuccess(false));
            }
            dbService.disconnect();
        } else if (cloudMessage instanceof RegistrationRequest registrationRequest) {
            log.info(registrationRequest.login + " " + "login" + " " + "password" + " " + registrationRequest.password + " " + registrationRequest.userName + " " + "username");
            dbService.connection();
            dbService.registration(registrationRequest.login, registrationRequest.password, registrationRequest.userName);
        } else if (cloudMessage instanceof RemoveLogin removeLogin) {
            Path path = Paths.get(removeLogin.removeLoginFromMap);
            System.out.println(path + " path");
            for (Path path1 : map.keySet()) {
                if (path1.equals(path)) {
                    map.remove(path);
                    dbService.disconnect();
                } else {
                    log.info(path + " else in cloudMessage instanceof RemoveLogin removeLogin");
                    log.info("not exist user in hashmap");
                }
            }
        } else {
            if (cloudMessage instanceof FileForDelete fileForDelete) {
                if (Files.exists(Path.of(fileForDelete.getName()))) {
                    Files.delete(Path.of(fileForDelete.getName()));
                    System.out.println("File deleted" + fileForDelete.getName());
                    ctx.writeAndFlush(new ListFiles(clientDir));
                    ctx.writeAndFlush(new ListFiles(currentDir));
                    ctx.writeAndFlush(new ListFiles(changeCurDirSerFolder));
                } else {
                    log.info("File not exist");
                }
            }
        }
    }
}






