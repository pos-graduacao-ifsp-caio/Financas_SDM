package br.com.caiomorceli.financas_sdm.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.caiomorceli.financas_sdm.R;
import br.com.caiomorceli.financas_sdm.adapter.ContaAdapter;
import br.com.caiomorceli.financas_sdm.dao.ContaDAO;
import br.com.caiomorceli.financas_sdm.model.Conta;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ContaDAO cDAO ;

    double saldoTotal = 0;

    private LinearLayout linearLayoutContas;
    private LinearLayout linearLayoutSaldoTotal;
    private RecyclerView recyclerViewContas;

    private List<Conta> contas = new ArrayList<>();
    private TextView empty;
    private TextView saldoTotalContas;

    private ContaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cDAO= new ContaDAO(this);
        empty = findViewById(R.id.lista_contas_vazia);   // textView   Sua lista está vazia
        linearLayoutContas = findViewById(R.id.linear_layout_contas);
        saldoTotalContas = findViewById(R.id.saldo_total_contas);

        linearLayoutSaldoTotal = findViewById(R.id.linear_layout_saldo_total);

        recyclerViewContas = findViewById(R.id.recycler_view_contas);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerViewContas.setLayoutManager(layout);

        adapter = new ContaAdapter(contas, this);
        recyclerViewContas.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);  // setado para null p/ permitir que os ícones da navigation possam ser exibidos.

        atualizarUI();
    }

    @Override
    public void onBackPressed() {
        // Faz o tratamento para fechar o NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            linearLayoutSaldoTotal.setVisibility(View.GONE);
            linearLayoutContas.setVisibility(View.VISIBLE);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_nova_conta:
                Intent intent = new Intent(getApplicationContext(), ContaActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.nav_saldo_total:
                List<Conta> contas;
                contas = cDAO.buscarTodasContas();
                saldoTotal = calcularSaldoTotal(contas);
                linearLayoutSaldoTotal.setVisibility(View.VISIBLE);
                linearLayoutContas.setVisibility(View.GONE);
                saldoTotalContas.setText(getResources().getText(R.string.cifrao) +String.valueOf(saldoTotal));
                break;
            case R.id.nav_sair:
                finish();  // fecha a aplicação
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                List<Conta> contas;
                contas = cDAO.buscarTodasContas();
                saldoTotal = 0;
                saldoTotal = calcularSaldoTotal(contas);
                saldoTotalContas.setText(getResources().getText(R.string.cifrao) +String.valueOf(saldoTotal));

                showSnackBar(getResources().getString(R.string.conta_cadastrada_com_sucesso));
                atualizarUI();
            }
        }

        if (requestCode == 2) {             // REQUEST CODE
            if (resultCode == RESULT_OK)
                showSnackBar(getResources().getString(R.string.conta_alterada_com_sucesso));

            if (resultCode == 3)                //RESULT CODE
                showSnackBar(getResources().getString(R.string.conta_excluida_com_sucesso));

            atualizarUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        configurarRecyclerView();
    }

    // Exibe informações das ações da aplicação em um snackBar.
    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout = findViewById(R.id.coordinator_layout_main);
        Snackbar.make(coordinatorlayout, msg, Snackbar.LENGTH_LONG).show();
    }

    // configura a implementacao do listener dos itens da recyclerView
    private void configurarRecyclerView() {
        adapter.setClickListener(new ContaAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Conta conta;

                conta = contas.get(position);
                Intent i = new Intent(getApplicationContext(), ContaActivity.class);
                i.putExtra("conta", conta);
                startActivityForResult(i, 2);

            }
        });
    }

    private double calcularSaldoTotal(List<Conta> contas){
        double saldoTotal = 0;
        for(Conta c : contas){
            saldoTotal += c.getSaldo();
        }

        return saldoTotal;
    }

    // atualiza a recycler de contas.
    private void atualizarUI() {
        contas.clear();

        contas.addAll(cDAO.buscarTodasContas());

        recyclerViewContas.getAdapter().notifyDataSetChanged();

        // verifica se existe alguma célula criada na recyclerView. Se não houver nenhuma coloca o texto de lista vazia na tela.
        if (recyclerViewContas.getAdapter().getItemCount() == 0) {
            recyclerViewContas.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
            recyclerViewContas.setVisibility(View.VISIBLE);
        }
    }
}
