package com.alexsmaliy.dl4s.index;

import java.util.Base64;

public class IndexUtils {
    private IndexUtils() { /* utils class */ }

    public static String encodeIndexName(String indexName) {
        return new String(Base64.getUrlEncoder().encode(indexName.getBytes()));
    }

    public static String decodeIndexName(String base64IndexName) {
        return new String(Base64.getUrlDecoder().decode(base64IndexName.getBytes()));
    }
}
