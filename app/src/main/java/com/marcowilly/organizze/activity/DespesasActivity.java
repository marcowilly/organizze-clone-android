package com.marcowilly.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marcowilly.organizze.R;
import com.marcowilly.organizze.framework.AlertUtil;
import com.marcowilly.organizze.framework.DateUtil;
import com.marcowilly.organizze.framework.EditTextUtil;
import com.marcowilly.organizze.model.Movimentacao;
import com.marcowilly.organizze.model.DAO.MovimentacaoDAO;
import com.marcowilly.organizze.model.Usuario;
import com.marcowilly.organizze.model.DAO.UsuarioDAO;

public final class DespesasActivity extends AppCompatActivity implements ValueEventListener, OnCompleteListener {

    private TextInputEditText edtData, edtCategoria, edtDescricao;
    private EditText edtValor;
    private double despesaTotal;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        edtValor = findViewById(R.id.edtValor);
        edtData = findViewById(R.id.edtData);
        edtCategoria = findViewById(R.id.edtCategoria);
        edtDescricao = findViewById(R.id.edtDescricao);

        //Preenche campo data com a data atual
        edtData.setText(DateUtil.dataAtual());
    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioDAO.recuperarDespesaTotal(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UsuarioDAO.getUsuarioRef().removeEventListener(this);
    }

    public void salvarDespesa(View view){
        String txtValor = EditTextUtil.getText(edtValor);
        final String txtData = EditTextUtil.getText(edtData);
        final String txtCategoria = EditTextUtil.getText(edtCategoria);
        final String txtDescricao = EditTextUtil.getText(edtDescricao);

        if(validarDespesa(txtData, txtCategoria, txtDescricao, txtValor)) {
            txtValor = txtValor.replace(",",".");
            final double despesaGerada = Double.parseDouble(txtValor);
            despesaTotal += despesaGerada;

            final Movimentacao movimentacao = new Movimentacao(txtData, txtCategoria, txtDescricao, Movimentacao.DESPESA, despesaGerada);
            UsuarioDAO.setDespesaTotal(despesaTotal);
            MovimentacaoDAO.salvar(movimentacao, txtData, this);
        }
    }

    public boolean validarDespesa(final String data, final String categoria, final String descricao, final String valor){
        if(valor.isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha o valor!", edtValor);
        else if(data.isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha a data!", edtData);
        else if(categoria.isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha a categoria!", edtCategoria);
        else if(descricao.isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha a descrição!", edtDescricao);
        else return true;
        return false;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        despesaTotal = snapshot.getValue(Usuario.class).getDespesaTotal();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {}

    @Override
    public void onComplete(@NonNull Task task) {
        if(task.isSuccessful()){
            finish();
        }else{
            AlertUtil.showSnackBarSuccess(this, "Erro ao adicionar despesa.");
        }
    }
}