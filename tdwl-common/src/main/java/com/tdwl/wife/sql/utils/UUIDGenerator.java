package com.tdwl.wife.sql.utils;

import java.util.UUID;

public class UUIDGenerator{
    
    public static String getUUID() {
        UUID uuid = UUID.randomUUID(); 
        return uuid.toString().replaceAll("-", "");
    }

}
