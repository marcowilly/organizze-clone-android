package com.marcowilly.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.marcowilly.organizze.R;
import com.marcowilly.organizze.framework.Base64Util;
import com.marcowilly.organizze.model.Usuario;
import com.marcowilly.organizze.model.DAO.UsuarioDAO;
import com.marcowilly.organizze.settings.FirebaseSetting;
import com.marcowilly.organizze.framework.AlertUtil;
import com.marcowilly.organizze.framework.EditTextUtil;

public final class CadastroActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtSenha;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().setTitle("Cadastro");

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
    }

    public void btnCadastrar(View view){
        usuario = new Usuario(EditTextUtil.getText(edtNome), EditTextUtil.getText(edtEmail), EditTextUtil.getText(edtSenha));

        if(usuario.getNome().isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha o nome!", edtNome);
        else if(usuario.getEmail().isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha o email!", edtEmail);
        else if(usuario.getSenha().isEmpty()) EditTextUtil.showAlertEditText(this,"Preencha a senha!", edtSenha);
        else cadastrarUsuario();
    }

    private void cadastrarUsuario(){
        FirebaseSetting.getAuth().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                usuario.setIdUsuario(Base64Util.codificarBase64(usuario.getEmail()));
                UsuarioDAO.salvar(usuario);
                finish();
            }else{
                String error = "";
                try{
                    throw task.getException();
                }catch (FirebaseAuthWeakPasswordException e){
                    error = "Digite uma senha mais forte!";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    error = "Digite um e-mail valido!";
                }catch (FirebaseAuthUserCollisionException e){
                    error = "Conta já cadastrada!";
                }catch (Exception e){
                    error = String.format("Erro ao cadastrar usuário: %s", e.getMessage());
                    e.printStackTrace();
                }
                AlertUtil.showSnackBarError(this, error);
            }
        });
    }
}