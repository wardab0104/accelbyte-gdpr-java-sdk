/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk.object;

import java.util.HashMap;
import java.util.Map;

public class DataGenerationResult {

    Map<String, byte[]> data;

    public DataGenerationResult(){
        data = new HashMap<>();
    }

    public Map<String, byte[]> getData(){
        return data;
    }

    public void setData(String moduleId, byte[] bytes){
        data.put(moduleId, bytes);
    }
}
