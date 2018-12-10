package br.com.caiomorceli.financas_sdm.views;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.Serializable;

import br.com.caiomorceli.financas_sdm.R;
import br.com.caiomorceli.financas_sdm.dao.ContaDAO;
import br.com.caiomorceli.financas_sdm.model.Conta;

public class ContaActivity extends AppCompatActivity implements View.OnClickListener {

    private Conta conta;
    private ContaDAO contaDAO;

    ViewHolder holder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        holder.botaoCadastrar = findViewById(R.id.btn_cadastrar_conta);

        holder.botaoCadastrar.setOnClickListener(this);  // passando a referência para o listener

        if (getIntent().hasExtra("conta")) {   // verifica se existe uma conta passada na intent.
            this.conta = (Conta) getIntent().getSerializableExtra("conta");

            holder.botaoCadastrar.setText(R.string.alterar_conta);

            holder.botaoExcluir = findViewById(R.id.btn_excluir_conta);
            holder.botaoConsultarExtrato = findViewById(R.id.btn_consultar_extrato);
            holder.botaoGerarExtrato = findViewById(R.id.btn_gerar_extrato);
            holder.editTxt_descricao = findViewById(R.id.editText_descricao_conta);
            holder.editTxt_descricao.setText(conta.getDescricao());
            holder.editTxt_saldo_inicial = findViewById(R.id.editText_saldo_inicial_conta);
            holder.editTxt_saldo_inicial.setText( Double.toString(conta.getSaldo()));

            holder.linearLayoutSelecaoExtrato = findViewById(R.id.linear_layout_selecao_extrato);
            holder.radioGroupTipoOperacaoExtrato = findViewById(R.id.radio_group_tipo_operacao_extrato);

            holder.spinnerExtratoMes = findViewById(R.id.spinner_extrato_mes);
            holder.spinnerExtratoCategorias = findViewById(R.id.spinner_extrato_categorias);

            int posicao = conta.getDescricao().indexOf(" ");

            if (posicao == -1) {
                posicao = conta.getDescricao().length();
            }

            setTitle(conta.getDescricao().substring(0,posicao));
            holder.botaoExcluir.setVisibility(View.VISIBLE);
            holder.botaoConsultarExtrato.setVisibility(View.VISIBLE);
            holder.botaoExcluir.setOnClickListener(this);
            holder.botaoConsultarExtrato.setOnClickListener(this);
            holder.botaoGerarExtrato.setOnClickListener(this);

        }

        contaDAO = new ContaDAO(this);
    }

    // infla o menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);  // Infla o menu;
        if (!getIntent().hasExtra("conta")) {       // se ñ passar a conta entao ñ apresenta os icones de debito e credito.

            MenuItem itemCredito = menu.findItem(R.id.cadastrar_credito);
            MenuItem itemDebito = menu.findItem(R.id.cadastrar_debito);
            itemCredito.setVisible(false);
            itemDebito.setVisible(false);
        }
        return true;
    }

    @Override   // Trata o vento de clique de cada item do menu direito do aplicativo.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.cadastrar_credito:
                Intent intentCredito = new Intent(getApplicationContext(), OperacaoMonetariaActivity.class);
                intentCredito.putExtra("conta", this.conta);
                intentCredito.putExtra("tipoOperacao", "credito");
                startActivityForResult(intentCredito, 1);
                return true;
            case R.id.cadastrar_debito:
                Intent intentDebito = new Intent(getApplicationContext(), OperacaoMonetariaActivity.class);
                intentDebito.putExtra("conta", this.conta);
                intentDebito.putExtra("tipoOperacao", "debito");
                startActivityForResult(intentDebito, 2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent resultIntent = new Intent();

        switch (id){
            case R.id.btn_cadastrar_conta:
                cadastrarConta();
                setResult(RESULT_OK,resultIntent);
                finish();
                break;
            case R.id.btn_excluir_conta:
                excluirConta();
                setResult(3, resultIntent);
                finish();
                break;
            case R.id.btn_consultar_extrato:
                exibirCamposParaExtrato();
                break;
            case R.id.btn_gerar_extrato:
                int id_radioBtn_selecionado = holder.radioGroupTipoOperacaoExtrato.getCheckedRadioButtonId();
                RadioButton radioBtnSelecionado = holder.radioGroupTipoOperacaoExtrato.findViewById(id_radioBtn_selecionado);
                gerarExtrato(String.valueOf(radioBtnSelecionado.getText()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.credito_cadastrado_com_sucesso));
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.debito_cadastrado_com_sucesso));
            }
        }

        this.conta = contaDAO.buscarConta(String.valueOf(this.conta.getId()));

        holder.editTxt_descricao.setText(this.conta.getDescricao());
        holder.editTxt_saldo_inicial.setText(String.valueOf(this.conta.getSaldo()));
    }

    // Exibe informações das ações da aplicação nesta activity em um snackBar.
    private void showSnackBar(String msg) {
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_conta);
        Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    private void excluirConta(){
        contaDAO.excluirConta(conta);
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);   // da um setResult  para setar o resultado e poder tratar na activity de baixo
        finish();       // fecha a activity e volta para a activity que estava na camada abaixo desta.
    }

    private void cadastrarConta() {
        String descricaoConta = ((EditText) findViewById(R.id.editText_descricao_conta)).getText().toString();
        String saldoConta = ((EditText) findViewById(R.id.editText_saldo_inicial_conta)).getText().toString();

        if (conta == null) {
            conta = new Conta();
        }

        conta.setDescricao(descricaoConta);
        conta.setSaldo(Double.parseDouble(saldoConta));

        contaDAO.salvaConta(conta);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    public void exibirCamposParaExtrato(){
        holder.botaoConsultarExtrato.setVisibility(View.GONE);
        holder.botaoCadastrar.setVisibility(View.GONE);
        holder.botaoExcluir.setVisibility(View.GONE);
        holder.linearLayoutSelecaoExtrato.setVisibility(View.VISIBLE);
        holder.botaoGerarExtrato.setVisibility(View.VISIBLE);

        holder.radioGroupTipoOperacaoExtrato.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int opcaoSelecionada) {

                switch (opcaoSelecionada){
                    case R.id.radio_button_extrato_mes:
                        holder.spinnerExtratoCategorias.setVisibility(View.GONE);
                        holder.spinnerExtratoMes.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_extrato_categoria:
                        holder.spinnerExtratoMes.setVisibility(View.GONE);
                        holder.spinnerExtratoCategorias.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_extrato_credito:
                        holder.spinnerExtratoMes.setVisibility(View.GONE);
                        holder.spinnerExtratoCategorias.setVisibility(View.GONE);
                        break;

                    case R.id.radio_button_extrato_debito:
                        holder.spinnerExtratoMes.setVisibility(View.GONE);
                        holder.spinnerExtratoCategorias.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void gerarExtrato(String tipoExtrato){
        String mesExtrato = "0";
        Intent intentGerarExtrato = new Intent(getApplicationContext(), GerarExtratoActivity.class);
        intentGerarExtrato.putExtra("conta", (Serializable) conta);

        switch (tipoExtrato){
            case "Mês":
                intentGerarExtrato.putExtra("tipo_extrato", tipoExtrato);
                mesExtrato = holder.spinnerExtratoMes.getSelectedItem().toString();
                break;

            case "Categoria":
                String categoriaOperacao = holder.spinnerExtratoCategorias.getSelectedItem().toString();
                intentGerarExtrato.putExtra("tipo_extrato", tipoExtrato);
                intentGerarExtrato.putExtra("categoria_extrato", categoriaOperacao);
                break;

            case "Crédito":
                intentGerarExtrato.putExtra("tipo_extrato", tipoExtrato);
                break;

            case "Débito":
                intentGerarExtrato.putExtra("tipo_extrato", tipoExtrato);
                break;
        }

        intentGerarExtrato.putExtra("mes_extrato", mesExtrato);
        startActivity(intentGerarExtrato);
    }

    private static class ViewHolder{
        EditText editTxt_descricao;
        EditText editTxt_saldo_inicial;

        Button botaoCadastrar;
        Button botaoExcluir;
        Button botaoConsultarExtrato;
        Button botaoGerarExtrato;

        RadioGroup radioGroupTipoOperacaoExtrato;

        Spinner spinnerExtratoMes;
        Spinner spinnerExtratoCategorias;

        LinearLayout linearLayoutSelecaoExtrato;
    }
}