package br.com.caiomorceli.financas_sdm.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.caiomorceli.financas_sdm.R;
import br.com.caiomorceli.financas_sdm.adapter.OperacaoMonetariaAdapter;
import br.com.caiomorceli.financas_sdm.dao.OperacaoMonetariaDAO;
import br.com.caiomorceli.financas_sdm.model.Conta;
import br.com.caiomorceli.financas_sdm.model.OperacaoMonetaria;

public class GerarExtratoActivity extends AppCompatActivity {

    private Conta conta;
    String mesExtrato = null;

    private OperacaoMonetariaDAO opmDAO ;
    private RecyclerView recyclerViewOperacoesMonetarias;

    private List<OperacaoMonetaria> operacoesMonetarias = new ArrayList<>();
    private TextView textViewListaOperacoesVazia;

    private OperacaoMonetariaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_extrato);

        this.conta = (Conta) getIntent().getSerializableExtra("conta");

        mesExtrato = (getIntent().getStringExtra("mes_extrato"));

        opmDAO= new OperacaoMonetariaDAO(this);
        textViewListaOperacoesVazia = findViewById(R.id.lista_operacoes_vazia);   // textView   Sua lista está vazia

        recyclerViewOperacoesMonetarias = findViewById(R.id.recycler_view_operacoes_monetarias);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerViewOperacoesMonetarias.setLayoutManager(layout);

        adapter = new OperacaoMonetariaAdapter(operacoesMonetarias, this);
        recyclerViewOperacoesMonetarias.setAdapter(adapter);

        gerarExtrato();
    }

    private void gerarExtrato(){
        String id_conta = String.valueOf(this.conta.getId());
        operacoesMonetarias.clear();

        String tipoExtrato = getIntent().getStringExtra("tipo_extrato");

        switch (tipoExtrato){
            case "Mês":
                int mesExtrato = Integer.parseInt(getIntent().getStringExtra("mes_extrato"));

                // CRIAR O METODO NO BANCO AQUI PARA  BUSCAR  OS CREDITOS E OS DEBITOS  DO MES QUE O CARA SELECIONAR. E GERAR O EXTRATO
                // aqui precisa saber se é credito ou debito  e saber qual o mes foi escolhido
                operacoesMonetarias.addAll(opmDAO.gerarExtratoPorMes(id_conta, String.valueOf(mesExtrato)));
                break;
            case "Categoria":
                String categoriaOperacao = getIntent().getStringExtra("categoria_extrato");
                operacoesMonetarias.addAll(opmDAO.gerarExtratoPorCategoria(id_conta, categoriaOperacao));
                break;
            case "Crédito":
                operacoesMonetarias.addAll(opmDAO.gerarExtratoCredito(id_conta));
                break;
            case "Débito":
                operacoesMonetarias.addAll(opmDAO.gerarExtratoDebito(id_conta));
                break;
        }

        recyclerViewOperacoesMonetarias.getAdapter().notifyDataSetChanged();

        // verifica se existe alguma célula criada na recyclerView. Se não houver nenhuma coloca o texto de lista vazia na tela.
        if (recyclerViewOperacoesMonetarias.getAdapter().getItemCount() == 0) {
            textViewListaOperacoesVazia.setVisibility(View.VISIBLE);
        }
        else {
            textViewListaOperacoesVazia.setVisibility(View.GONE);
        }
    }
}
