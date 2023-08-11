/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk;

import net.accelbyte.gdpr.sdk.utils.FileHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FileHelperTest {

    @Test
    public void testCreateSummary() {

        String result = FileHelper.createSummaryJson("mynamespace", "myuserid",  Arrays.asList(new String[]{"module1", "module2"}));
        assertEquals(result, "{\"namespace\":\"mynamespace\",\"userId\":\"myuserid\",\"modules\":[\"module1\",\"module2\"]}");
    }
    @Test
    public void testCreateSummaryWithEmptyData() {
        Map<String, byte[]> data = new HashMap(){{}};
        String result = FileHelper.createSummaryJson("mynamespace", "myuserid", new ArrayList<>());
        assertEquals(result, "{\"namespace\":\"mynamespace\",\"userId\":\"myuserid\",\"modules\":[]}");
    }
}
