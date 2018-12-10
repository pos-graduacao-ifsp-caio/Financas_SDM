package br.com.caiomorceli.financas_sdm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.caiomorceli.financas_sdm.model.Conta;
import br.com.caiomorceli.financas_sdm.model.OperacaoMonetaria;

public class OperacaoMonetariaDAO {

    private SQLiteDatabase database;   // CLASSE que provê os métodos de manipulação dos dados no banco insert/update/delete
    private SQLiteHelper dbHelper;

    public OperacaoMonetariaDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public void cadastrarCredito(OperacaoMonetaria opm) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.DESCRICAO_CREDITO, opm.getDescricao());
        values.put(SQLiteHelper.VALOR_CREDITO, opm.getValor());
        values.put(SQLiteHelper.TIPO_CREDITO, opm.getTipo());
        values.put(SQLiteHelper.MES_CREDITO, opm.getMes());
        values.put(SQLiteHelper.ID_CONTA, opm.getId_conta());

        database.insert(SQLiteHelper.TABELA_CREDITO, null, values);

        database.close();
    }

    public void cadastrarDebito(OperacaoMonetaria opm) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.DESCRICAO_DEBITO, opm.getDescricao());
        values.put(SQLiteHelper.VALOR_DEBITO, opm.getValor());
        values.put(SQLiteHelper.TIPO_DEBITO, opm.getTipo());
        values.put(SQLiteHelper.MES_DEBITO, opm.getMes());
        values.put(SQLiteHelper.ID_CONTA, opm.getId_conta());

        database.insert(SQLiteHelper.TABELA_DEBITO, null, values);

        database.close();
    }

    public List<OperacaoMonetaria> gerarExtratoCredito(String id_conta) {
        database = dbHelper.getReadableDatabase();
        List<OperacaoMonetaria> operacoesMonetarias = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.ID_CREDITO, SQLiteHelper.DESCRICAO_CREDITO, SQLiteHelper.VALOR_CREDITO };

        String where = SQLiteHelper.ID_CONTA + " = ?";
        String[] argWhere = new String[]{ id_conta };

        cursor = database.query(SQLiteHelper.TABELA_CREDITO, cols, where , argWhere,
                null, null, SQLiteHelper.DESCRICAO_CREDITO);   // ordena a consulta pela descricao da operacao.

        while (cursor.moveToNext()) {
            OperacaoMonetaria operacaoMonetaria = new OperacaoMonetaria();

            operacaoMonetaria.setId_operacao(cursor.getInt(0));
            operacaoMonetaria.setDescricao(cursor.getString(1));
            operacaoMonetaria.setValor(cursor.getDouble(2));

            operacoesMonetarias.add(operacaoMonetaria);
        }

        cursor.close();

        database.close();
        return operacoesMonetarias;
    }

    public List<OperacaoMonetaria> gerarExtratoDebito(String id_conta) {
        database = dbHelper.getReadableDatabase();
        List<OperacaoMonetaria> operacoesMonetarias = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.ID_DEBITO, SQLiteHelper.DESCRICAO_DEBITO, SQLiteHelper.VALOR_DEBITO };

        String where = SQLiteHelper.ID_CONTA + " = ?";
        String[] argWhere = new String[]{ id_conta };

        cursor = database.query(SQLiteHelper.TABELA_DEBITO, cols, where , argWhere,
                null, null, SQLiteHelper.DESCRICAO_DEBITO);   // ordena a consulta pela descricao da operacao.

        while (cursor.moveToNext()) {
            OperacaoMonetaria operacaoMonetaria = new OperacaoMonetaria();

            operacaoMonetaria.setId_operacao(cursor.getInt(0));
            operacaoMonetaria.setDescricao(cursor.getString(1));
            operacaoMonetaria.setValor(cursor.getDouble(2));

            operacoesMonetarias.add(operacaoMonetaria);
        }

        cursor.close();

        database.close();
        return operacoesMonetarias;
    }

    public void atualizarSaldo (Conta conta) {
        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.DESCRICAO_CONTA, conta.getDescricao());
        values.put(SQLiteHelper.SALDO_CONTA, conta.getSaldo());

        database = dbHelper.getWritableDatabase();
        database.update(SQLiteHelper.TABELA_CONTA, values, SQLiteHelper.ID_CONTA + " = " + conta.getId(), null);
        database.close();
    }

    public List<OperacaoMonetaria> gerarExtratoPorMes(String id_conta, String mes) {
        database = dbHelper.getReadableDatabase();
        List<OperacaoMonetaria> operacoesMes = new ArrayList<>();

        Cursor cursorCredito;
        Cursor cursorDebito;

        String[] colsCredito = new String[]{SQLiteHelper.ID_CREDITO, SQLiteHelper.DESCRICAO_CREDITO, SQLiteHelper.VALOR_CREDITO};
        String[] colsDebito = new String[]{SQLiteHelper.ID_DEBITO, SQLiteHelper.DESCRICAO_DEBITO, SQLiteHelper.VALOR_DEBITO};

        String whereCredito = SQLiteHelper.ID_CONTA +" = ? AND " + SQLiteHelper.MES_CREDITO + " = ?";
        String whereDebito = SQLiteHelper.ID_CONTA +" = ? AND " + SQLiteHelper.MES_DEBITO + " = ?";

        String[] argWhere = new String[]{id_conta, mes};

        cursorCredito = database.query(SQLiteHelper.TABELA_CREDITO, colsCredito, whereCredito, argWhere,
                null, null, SQLiteHelper.DESCRICAO_CREDITO);   // ordena a consulta pela descricao da operacao.

        cursorDebito = database.query(SQLiteHelper.TABELA_DEBITO, colsDebito, whereDebito, argWhere,
                null, null, SQLiteHelper.DESCRICAO_DEBITO);   // ordena a consulta pela descricao da operacao.

        while (cursorCredito.moveToNext()) {
            OperacaoMonetaria operacaoMonetaria = new OperacaoMonetaria();

            operacaoMonetaria.setId_operacao(cursorCredito.getInt(0));
            operacaoMonetaria.setDescricao(cursorCredito.getString(1));
            operacaoMonetaria.setValor(cursorCredito.getDouble(2));

            operacoesMes.add(operacaoMonetaria);
        }

        while (cursorDebito.moveToNext()) {
            OperacaoMonetaria operacaoMonetaria = new OperacaoMonetaria();

            operacaoMonetaria.setId_operacao(cursorDebito.getInt(0));
            operacaoMonetaria.setDescricao(cursorDebito.getString(1));
            operacaoMonetaria.setValor(cursorDebito.getDouble(2));

            operacoesMes.add(operacaoMonetaria);
        }

        cursorCredito.close();
        cursorDebito.close();

        database.close();
        return operacoesMes;
    }

    public List<OperacaoMonetaria> gerarExtratoPorCategoria(String id_conta, String categoriaOperacao) {
        database = dbHelper.getReadableDatabase();
        List<OperacaoMonetaria> operacoesMonetarias = new ArrayList<>();

        Cursor cursor;

        // Verifica se é uma categoria que se caracteriza como crédito ou como débito
        if(categoriaOperacao.equals("Alimentação") || categoriaOperacao.equals("Energia") || categoriaOperacao.equals("Água")    || categoriaOperacao.equals("Saúde") || categoriaOperacao.equals("Tarifa Bancária") || categoriaOperacao.equals("Lazer") || categoriaOperacao.equals("Educação") || categoriaOperacao.equals("Moradia") || categoriaOperacao.equals("Transporte")){
            String[] cols = new String[]{SQLiteHelper.ID_DEBITO, SQLiteHelper.DESCRICAO_DEBITO, SQLiteHelper.VALOR_DEBITO};

            String where = SQLiteHelper.ID_CONTA +" = ? AND " + SQLiteHelper.TIPO_DEBITO + " = ?";
            String[] argWhere = new String[]{id_conta, categoriaOperacao};

            cursor = database.query(SQLiteHelper.TABELA_DEBITO, cols, where, argWhere,
                    null, null, SQLiteHelper.DESCRICAO_DEBITO);   // ordena a consulta pela descricao da operacao.
        }   else {

            String[] cols = new String[]{SQLiteHelper.ID_CREDITO, SQLiteHelper.DESCRICAO_CREDITO, SQLiteHelper.VALOR_CREDITO};

            String where = SQLiteHelper.ID_CONTA +" = ? AND " + SQLiteHelper.TIPO_CREDITO + " = ?";
            String[] argWhere = new String[]{id_conta, categoriaOperacao};

            cursor = database.query(SQLiteHelper.TABELA_CREDITO, cols, where, argWhere,
                    null, null, SQLiteHelper.DESCRICAO_CREDITO);   // ordena a consulta pela descricao da operacao.

        }

        while (cursor.moveToNext()) {
            OperacaoMonetaria operacaoMonetaria = new OperacaoMonetaria();

            operacaoMonetaria.setId_operacao(cursor.getInt(0));
            operacaoMonetaria.setDescricao(cursor.getString(1));
            operacaoMonetaria.setValor(cursor.getDouble(2));

            operacoesMonetarias.add(operacaoMonetaria);
        }

        cursor.close();

        database.close();
        return operacoesMonetarias;
    }
}
