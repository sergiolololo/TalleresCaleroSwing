package com.taller.utils;// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// [START drive_quickstart]

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/* class to demonstrate use of Drive files list API */
public class GoogleDrive {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
    private static NetHttpTransport HTTP_TRANSPORT;
    private static Drive service;

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleDrive.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("sherrerahiguera@gmail.com");
    }

    private static void initService() throws GeneralSecurityException, IOException {
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void downloadFile() throws GeneralSecurityException, IOException {
        initService();
        String pageToken = null;
        String fileId = null;
        do{
            FileList result = service.files().list()
                    .setQ("'1NT9ijgcNPGjirkui_wzgzUOqCSTXXcSj' in parents") // carpeta COCHES
                    .setPageSize(10)
                    .setPageToken(pageToken)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files:");
                for (File file : files) {
                    System.out.println(file.getName());
                    if(file.getName().equals("miBaseDeDatos.script")){
                        fileId = file.getId();
                        java.io.File backUp = new java.io.File("./DatoJava/HSQLDB/miBaseDeDatos.script");
                        Files.copy(backUp.toPath(), new java.io.File("./DatoJava/HSQLDB/miBaseDeDatos_backup.script").toPath(), StandardCopyOption.REPLACE_EXISTING);
                        ByteArrayOutputStream out = downloadFile(file.getId());
                        try(OutputStream outputStream = new FileOutputStream("./DatoJava/HSQLDB/miBaseDeDatos.script")) {
                            out.writeTo(outputStream);
                        }
                    }
                }
            }
            pageToken = result.getNextPageToken();
        }while (pageToken != null && fileId == null);
    }

    private static ByteArrayOutputStream downloadFile(String fileId) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.files().get(fileId).executeMediaAndDownloadTo(out);
        return out;
    }

    public static void uploadFile() throws IOException, GeneralSecurityException {
        initService();
        String pageToken = null;
        String fileId = null;
        do{
            FileList result = service.files().list()
                    .setQ("'1NT9ijgcNPGjirkui_wzgzUOqCSTXXcSj' in parents") // carpeta COCHES
                    .setPageSize(10)
                    .setPageToken(pageToken)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            } else {
                System.out.println("Files:");
                for (File file : files) {
                    System.out.println(file.getName());
                    if(file.getName().equals("miBaseDeDatos.script")){
                        fileId = file.getId();
                        break;
                    }
                }
            }
            pageToken = result.getNextPageToken();
        }while (pageToken != null && fileId == null);

        File fileMetadata = new File();
        fileMetadata.setName("miBaseDeDatos.script");
        fileMetadata.setParents(Collections.singletonList("1NT9ijgcNPGjirkui_wzgzUOqCSTXXcSj"));

        java.io.File filePath = new java.io.File("./DatoJava/HSQLDB/miBaseDeDatos.script");
        FileContent mediaContent = new FileContent("text/csv", filePath);

        if(fileId == null){
            service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
        }else{
            service.files().update(fileId, null, mediaContent).execute();
        }

    }
}