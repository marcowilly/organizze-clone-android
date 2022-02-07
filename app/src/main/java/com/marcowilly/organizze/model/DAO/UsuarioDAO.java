package com.marcowilly.organizze.model.DAO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcowilly.organizze.framework.Base64Util;
import com.marcowilly.organizze.model.Usuario;
import com.marcowilly.organizze.settings.FirebaseSetting;

public abstract class UsuarioDAO {

    public static void salvar(final Usuario usuario){
        getUsuarioRef().setValue(usuario);
    }

    public static void recuperarReceitaTotal(ValueEventListener valueEventListener){
        getUsuarioRef().addValueEventListener(valueEventListener);
    }

    public static void setReceitaTotal(Double receitaTotal) {
        getUsuarioRef().child("receitaTotal").setValue(receitaTotal);
    }
    public static DatabaseReference getUsuarioRef(){
        return FirebaseSetting.getDatabase()
                .child("usuarios")
                .child(Base64Util.codificarBase64(FirebaseSetting.getAuth().getCurrentUser().getEmail()));
    }

    public static void recuperarDespesaTotal(ValueEventListener valueEventListener){
        getUsuarioRef().addValueEventListener(valueEventListener);
    }

    public static void setDespesaTotal(double despesaTotal) {
        getUsuarioRef().child("despesaTotal").setValue(despesaTotal);
    }

    public static void deslogarUsuario(){
        FirebaseSetting.getAuth().signOut();
    }

    public static void recuperarResumo(final ValueEventListener valueEventListener){
        getUsuarioRef().addValueEventListener(valueEventListener);
    }
}
