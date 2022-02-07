package com.marcowilly.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marcowilly.organizze.R;
import com.marcowilly.organizze.adapter.AdapterMovimentacao;
import com.marcowilly.organizze.framework.AlertUtil;
import com.marcowilly.organizze.framework.DateUtil;
import com.marcowilly.organizze.model.DAO.MovimentacaoDAO;
import com.marcowilly.organizze.model.Movimentacao;
import com.marcowilly.organizze.model.Usuario;
import com.marcowilly.organizze.model.DAO.UsuarioDAO;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class MainActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView txtSaudacao, txtSaldo;
    private final List<Movimentacao> movimentacaoList = new ArrayList<>();
    private String mesAno;
    private Movimentacao movimentacao;
    private RecyclerView recyclerView;
    private ValueEventListener valueEventListenerMovimentacao;
    private ValueEventListener valueEventListenerUsuario;
    private AdapterMovimentacao adapterMovimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        txtSaldo = findViewById(R.id.txtSaldo);
        txtSaudacao = findViewById(R.id.txtSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recycler);
        configurarCalendarView();
        swipe();

        //Configurando adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacaoList, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
    }

    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(RecyclerView.ViewHolder viewHolder){
        AlertUtil.showAlert(this,
                "Excluir Movimentação",
                "Você tem certeza que deseja excluir essa movimentação?",
                false,
                (dialogInterface, i) -> {
                    int position = viewHolder.getAdapterPosition();
                    movimentacao = movimentacaoList.get(position);
                    MovimentacaoDAO.excluirMovimentacao(mesAno, movimentacao.getKey());
                    adapterMovimentacao.notifyItemRemoved(position);
                    atualizarSaldo();
                },
                ((dialogInterface, i) -> adapterMovimentacao.notifyDataSetChanged()));
    }

    public void atualizarSaldo(){
        valueEventListenerUsuario = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final Usuario usuario = snapshot.getValue(Usuario.class);
                if(movimentacao.getTipo().equals(Movimentacao.RECEITA)){
                    double receitaTotal = usuario.getReceitaTotal() - movimentacao.getValor();
                    UsuarioDAO.getUsuarioRef().child("receitaTotal").setValue(receitaTotal);
                }else{
                    double despesaTotal = usuario.getDespesaTotal() - movimentacao.getValor();
                    UsuarioDAO.getUsuarioRef().child("despesaTotal").setValue(despesaTotal);
                }
                UsuarioDAO.getUsuarioRef().removeEventListener(valueEventListenerUsuario);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        };
        UsuarioDAO.recuperarReceitaTotal(valueEventListenerUsuario);
    }

    @Override
    protected void onStart() {
        super.onStart();
        valueEventListenerUsuario = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final Usuario usuario = snapshot.getValue(Usuario.class);
                txtSaudacao.setText(String.format("Olá, %s", usuario.getNome()));
                txtSaldo.setText(String.format("R$ %.2f", (usuario.getReceitaTotal() - usuario.getDespesaTotal())));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        };
        UsuarioDAO.recuperarResumo(valueEventListenerUsuario);
        recuperarMovimentacoes();
    }

    public void recuperarMovimentacoes(){
        valueEventListenerMovimentacao = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                movimentacaoList.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setKey(dados.getKey());
                    movimentacaoList.add(movimentacao);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        };
        MovimentacaoDAO.recuperar(mesAno, valueEventListenerMovimentacao);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UsuarioDAO.getUsuarioRef().removeEventListener(valueEventListenerUsuario);
        MovimentacaoDAO.getMovimentacaoRef().removeEventListener(valueEventListenerMovimentacao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuSair){
            UsuarioDAO.deslogarUsuario();
            startActivity(new Intent(this, StartActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configurarCalendarView(){
        calendarView.setTitleMonths(new CharSequence[]{"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"});
        mesAno = DateUtil.mouthYearOnDate(calendarView.getCurrentDate());
        calendarView.setOnMonthChangedListener((widget, date) -> {
            mesAno = DateUtil.mouthYearOnDate(date);
            MovimentacaoDAO.getMovimentacaoRef().removeEventListener(valueEventListenerMovimentacao);
            recuperarMovimentacoes();
        });
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }
}