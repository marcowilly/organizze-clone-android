package com.marcowilly.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.marcowilly.organizze.R;
import com.marcowilly.organizze.framework.AlertUtil;
import com.marcowilly.organizze.framework.EditTextUtil;
import com.marcowilly.organizze.model.Usuario;
import com.marcowilly.organizze.settings.FirebaseSetting;

public final class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
    }

    public void btnEntrar(View view){
        usuario = new Usuario(EditTextUtil.getText(edtEmail), EditTextUtil.getText(edtSenha));

        if(usuario.getEmail().isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha o email!", edtEmail);
        else if (usuario.getSenha().isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha a senha!", edtSenha);
        else logarUsuario();
    }

    private void logarUsuario(){
        FirebaseSetting.getAuth().signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                abrirTelaPrincipal();
            }else{
                String error = "";
                try{
                    throw task.getException();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    error = "Dados inválidos!";
                }catch (FirebaseAuthInvalidUserException e){
                    error = "Usuario não cadastrado!";
                }catch (Exception e){
                    error = String.format("Erro ao fazer login do usuário: %s", e.getMessage());
                    e.printStackTrace();
                }
                AlertUtil.showSnackBarError(this, error);
            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
