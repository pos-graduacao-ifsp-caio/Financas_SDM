package br.com.caiomorceli.financas_sdm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.caiomorceli.financas_sdm.model.Conta;

public class ContaDAO {

    private SQLiteDatabase database;   // CLASSE que provê os métodos de manipulação dos dados no banco insert/update/delete
    private SQLiteHelper dbHelper;

    public ContaDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public void salvaConta(Conta c) {               // esse método é usado para  cadastrar e atualizar
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.DESCRICAO_CONTA, c.getDescricao());
        values.put(SQLiteHelper.SALDO_CONTA, c.getSaldo());


        if (c.getId() > 0) {    // se o id for maior que 0  então significa que é uma conta que será atualizada.
            database.update(SQLiteHelper.TABELA_CONTA, values, SQLiteHelper.ID_CONTA + "=" + c.getId(), null);

        }    else {             // se o id for menor ou igual a 0  então significa que é uma conta que será cadastrada.
            database.insert(SQLiteHelper.TABELA_CONTA, null, values);
        }

        database.close();
    }

    public void excluirConta(Conta c) {
        database = dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.TABELA_CONTA, SQLiteHelper.ID_CONTA + "=" + c.getId(), null);
        database.close();
    }


    public Conta buscarConta(String id_conta) {
        database = dbHelper.getReadableDatabase();
        Conta conta = null;

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.ID_CONTA, SQLiteHelper.DESCRICAO_CONTA, SQLiteHelper.SALDO_CONTA };

        String where = SQLiteHelper.ID_CONTA + " = ?";
        String[] argWhere = new String[]{ id_conta };

        cursor = database.query(SQLiteHelper.TABELA_CONTA, cols, where , argWhere, null, null, null);

        if (cursor.moveToFirst()) {
            conta = new Conta();

            conta.setId(cursor.getInt(0));
            conta.setDescricao(cursor.getString(1));
            conta.setSaldo(cursor.getDouble(2));
        }

        cursor.close();

        database.close();
        return conta;
    }

    public List<Conta> buscarTodasContas() {
        database = dbHelper.getReadableDatabase();
        List<Conta> contas = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] {
                SQLiteHelper.ID_CONTA,
                SQLiteHelper.DESCRICAO_CONTA,
                SQLiteHelper.SALDO_CONTA
        };

        cursor = database.query(SQLiteHelper.TABELA_CONTA, cols, null , null,
                null, null, SQLiteHelper.DESCRICAO_CONTA);   // ordena a consulta pela descricao da conta

        while (cursor.moveToNext()) {
            Conta conta = new Conta();

            conta.setId(cursor.getInt(0));
            conta.setDescricao(cursor.getString(1));
            conta.setSaldo(cursor.getDouble(2));

            contas.add(conta);
        }

        cursor.close();

        database.close();
        return contas;
    }

        // altera a coluna que informa se o contato está favoritado na lista de contatos ou não.
        /*
        public void alterarContatoFavorito(Contato c){

            ContentValues values = new ContentValues();

            values.put(SQLiteHelper.KEY_NAME, c.getNome());
            values.put(SQLiteHelper.KEY_FONE, c.getFone());
            values.put(SQLiteHelper.KEY_FONE_SECUNDARIO, c.getFoneSecundario());
            values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
            values.put(SQLiteHelper.KEY_BIRTHDAY_DATE, c.getBirthdayDate());
            values.put(SQLiteHelper.KEY_IS_FAVORED, c.getIsFavored());

            database = dbHelper.getWritableDatabase();
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "=" + c.getId(), null);
            database.close();
        }                               */
}

