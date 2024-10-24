package br.com.sgdrs.factories;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class UtilsFactory {
    public static UUID getRandomUUID(){
        return randomUUID();
    }
}
