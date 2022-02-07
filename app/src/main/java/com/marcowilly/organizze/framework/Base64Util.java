package com.marcowilly.organizze.framework;

import android.util.Base64;

public abstract class Base64Util {

    public static String codificarBase64(final String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificarBase64(final String textoCodificado){
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
