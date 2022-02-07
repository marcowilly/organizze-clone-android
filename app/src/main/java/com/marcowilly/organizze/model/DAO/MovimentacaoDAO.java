package com.marcowilly.organizze.model.DAO;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marcowilly.organizze.framework.Base64Util;
import com.marcowilly.organizze.framework.DateUtil;
import com.marcowilly.organizze.model.Movimentacao;
import com.marcowilly.organizze.settings.FirebaseSetting;

public abstract class MovimentacaoDAO {

    public static void salvar(final Movimentacao movimentacao, final String data, final OnCompleteListener onCompleteListener){
        getMovimentacaoRef()
                .child(DateUtil.mouthYearOnDate(data))
                .push()
                .setValue(movimentacao).addOnCompleteListener(onCompleteListener);
    }

    public static DatabaseReference getMovimentacaoRef(){
        return FirebaseSetting.getDatabase()
                .child("movimentacoes")
                .child(Base64Util.codificarBase64(FirebaseSetting.getAuth().getCurrentUser().getEmail()));
    }


    public static void recuperar(final String mesAno, final ValueEventListener onCompleteListener){
        getMovimentacaoRef()
                .child(mesAno)
                .addValueEventListener(onCompleteListener);
    }

    public static void excluirMovimentacao(final String mesAno, final String key){
        getMovimentacaoRef()
                .child(mesAno)
                .child(key)
                .removeValue();
    }

}
