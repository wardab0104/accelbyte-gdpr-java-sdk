/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileHelper {

    public static byte[] CreateZipFile(Map<String, byte[]> data, String namespace, String userId){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            List<String> includedModules = new ArrayList<>();
            for (Map.Entry<String, byte[]> entry : data.entrySet()) {
                if(isEmptyJson(entry.getValue())) {
                    continue;
                }

                ZipParameters zipParameters = new ZipParameters();
                zipParameters.setFileNameInZip(entry.getKey());
                zos.putNextEntry(zipParameters);
                zos.write(entry.getValue());
                zos.closeEntry();

                includedModules.add(entry.getKey());
            }

            // if none module included, then no need to create zip file
            if (includedModules.size() == 0){
                return null;
            }

            // create summary file
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setFileNameInZip("summary.json");
            zos.putNextEntry(zipParameters);
            String summaryJson = createSummaryJson(namespace, userId, includedModules);
            zos.write(summaryJson.getBytes());
            zos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    public static String createSummaryJson(String namespace, String userId, List<String> modules){
        JsonObject json = new JsonObject();
        json.addProperty("namespace", namespace);
        json.addProperty("userId", userId);
        json.add("modules", new Gson().toJsonTree(modules));

        return json.toString();
    }

    private static boolean isEmptyJson(byte[] bytes){
        if (bytes == null) {
            return true;
        }
        String data = new String(bytes);
        return data.equals("") || data.equals("{}") || data.equals("[]");
    }
}
