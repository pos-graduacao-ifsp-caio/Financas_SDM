package br.com.caiomorceli.financas_sdm.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import br.com.caiomorceli.financas_sdm.R;
import br.com.caiomorceli.financas_sdm.dao.OperacaoMonetariaDAO;
import br.com.caiomorceli.financas_sdm.model.Conta;
import br.com.caiomorceli.financas_sdm.model.OperacaoMonetaria;

public class OperacaoMonetariaActivity extends AppCompatActivity implements View.OnClickListener {

    private Conta conta;
    private String tipoOperacao;
    private ViewHolder holder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacao_monetaria);

        this.conta = (Conta) getIntent().getSerializableExtra("conta");

        holder.spinnerCredito = findViewById(R.id.spinner_categorias_credito);
        holder.spinnerDebito = findViewById(R.id.spinner_categorias_debito);
        holder.spinnerMesOperacao = findViewById(R.id.spinner_mes_operacao);

        holder.editTextDescricaoOperacao = findViewById(R.id.editText_descricao_operacao);
        holder.editTextValorOperacao = findViewById(R.id.editText_valor_operacao);

        holder.radioGroupOperacaoSeRepete = findViewById(R.id.radio_group_operacao_se_repete);

        holder.linearLayoutPeriodoRepeticao = findViewById(R.id.linear_layout_periodo_repeticao);

        holder.btnCadastrarOperacao = findViewById(R.id.btn_cadastrar_operacao);

        holder.btnCadastrarOperacao.setOnClickListener(this);

        tipoOperacao = getIntent().getStringExtra("tipoOperacao");

        if(tipoOperacao.equals("credito")){
            holder.spinnerCredito.setVisibility(View.VISIBLE);
        }   else{
            holder.spinnerDebito.setVisibility(View.VISIBLE);
        }

        holder.radioGroupOperacaoSeRepete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int opcaoSelecionada) {

                switch (opcaoSelecionada){
                    case R.id.radio_button_sim_repete:
                        holder.linearLayoutPeriodoRepeticao.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_nao_repete:
                        holder.linearLayoutPeriodoRepeticao.setVisibility(View.GONE);
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String categoriaOperacao;
        String mesOperacao;

        switch (id){
            case R.id.btn_cadastrar_operacao:
                if(tipoOperacao.equals("credito")){
                    categoriaOperacao = holder.spinnerCredito.getSelectedItem().toString();
                }   else{
                        categoriaOperacao = holder.spinnerDebito.getSelectedItem().toString();
                    }

                mesOperacao = holder.spinnerMesOperacao.getSelectedItem().toString();

                OperacaoMonetaria opm = new OperacaoMonetaria();
                opm.setDescricao(holder.editTextDescricaoOperacao.getText().toString());
                opm.setValor(Double.parseDouble(holder.editTextValorOperacao.getText().toString()));
                opm.setTipo(categoriaOperacao);
                opm.setMes(mesOperacao);
                opm.setId_conta(this.conta.getId());

                if(this.tipoOperacao.equals("credito")){
                    cadastrarCredito(opm);
                    double saldoAntigo = this.conta.getSaldo();
                    double valor = Double.parseDouble(holder.editTextValorOperacao.getText().toString());
                    double novoSaldo = (saldoAntigo + valor);
                    this.conta.setSaldo(novoSaldo);  // realiza a conta para atualizar o saldo
                    atualizarSaldo(this.conta);
                }   else{
                    cadastrarDedito(opm);
                    double saldoAntigo = this.conta.getSaldo();
                    double valor = Double.parseDouble(holder.editTextValorOperacao.getText().toString());
                    double novoSaldo = (saldoAntigo - valor);
                    this.conta.setSaldo( novoSaldo );  // realiza a conta para atualizar o saldo
                    atualizarSaldo(this.conta);
                }

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();

                break;
            case R.id.btn_excluir_operacao:
                break;
        }
    }

    private void cadastrarCredito(OperacaoMonetaria opm){
        OperacaoMonetariaDAO dao = new OperacaoMonetariaDAO(this);
        dao.cadastrarCredito(opm);
    }

    private void cadastrarDedito(OperacaoMonetaria opm){
        OperacaoMonetariaDAO dao = new OperacaoMonetariaDAO(this);
        dao.cadastrarDebito(opm);
    }

    private void atualizarSaldo(Conta conta){
        OperacaoMonetariaDAO dao = new OperacaoMonetariaDAO(this);
        dao.atualizarSaldo(conta);
    }

    private static class ViewHolder{
        EditText editTextDescricaoOperacao;
        EditText editTextValorOperacao;

        RadioGroup radioGroupOperacaoSeRepete;

        Spinner spinnerCredito;
        Spinner spinnerDebito;
        Spinner spinnerMesOperacao;

        LinearLayout linearLayoutPeriodoRepeticao;

        Button btnCadastrarOperacao;
    }
}
