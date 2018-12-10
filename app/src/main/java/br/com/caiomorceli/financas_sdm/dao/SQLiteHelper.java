package br.com.caiomorceli.financas_sdm.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String NOME_DATABASE = "financas.db";

    public static final String TABELA_CONTA = "conta";
    public static final String ID_CONTA = "id_conta";
    public static final String DESCRICAO_CONTA = "descricao_conta";
    public static final String SALDO_CONTA = "saldo_conta";

    public static final String TABELA_CREDITO = "credito";
    public static final String ID_CREDITO = "id_credito";
    public static final String DESCRICAO_CREDITO = "descricao_credito";
    public static final String VALOR_CREDITO = "valor_credito";
    public static final String TIPO_CREDITO = "tipo_credito";
    public static final String MES_CREDITO = "mes_credito";

    public static final String TABELA_DEBITO = "debito";
    public static final String ID_DEBITO = "id_debito";
    public static final String DESCRICAO_DEBITO = "descricao_debito";
    public static final String VALOR_DEBITO = "valor_debito";
    public static final String TIPO_DEBITO = "tipo_debito";
    public static final String MES_DEBITO = "mes_debito";

    private static final int VERSAO_DATABASE = 1;

    private static final String CRIAR_TABELA_CONTA = "CREATE TABLE "+ TABELA_CONTA +" (" +
            ID_CONTA  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DESCRICAO_CONTA + " TEXT NOT NULL, " +
            SALDO_CONTA + " REAL NOT NULL)";

    //private static final String CRIAR_TABELA_CREDITO = "CREATE TABLE credito (id_credito INTEGER PRIMARY KEY AUTOINCREMENT, valor_credito INTEGER, tipo_credito TEXT, id_conta INTEGER)"; // FUNCIONOU
    private static final String CRIAR_TABELA_CREDITO = "CREATE TABLE credito (id_credito INTEGER PRIMARY KEY AUTOINCREMENT, descricao_credito TEXT, valor_credito INTEGER, tipo_credito TEXT, mes_credito TEXT, id_conta INTEGER, FOREIGN KEY(id_conta) REFERENCES conta(id_conta))"; // funcionou
    private static final String CRIAR_TABELA_DEBITO = "CREATE TABLE debito (id_debito INTEGER PRIMARY KEY AUTOINCREMENT, descricao_debito TEXT, valor_debito INTEGER, tipo_debito TEXT, mes_debito TEXT, id_conta INTEGER, FOREIGN KEY(id_conta) REFERENCES conta(id_conta))"; // funcionou

    /*
    private static final String CRIAR_TABELA_CREDITO = "CREATE TABLE "+ TABELA_CREDITO +" (" +
            ID_CREDITO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            VALOR_CREDITO + " REAL NOT NULL, " +
            TIPO_CREDITO + " TEXT NOT NULL, " +
            ID_CONTA + "INTEGER NOT NULL, " +
            "FOREIGN KEY("+ID_CONTA+") " +"REFERENCES "+TABELA_CONTA+"("+ID_CONTA+"))";

    private static final String CRIAR_TABELA_DEBITO = "CREATE TABLE "+ TABELA_DEBITO +" (" +
            ID_DEBITO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            VALOR_DEBITO + " REAL NOT NULL, " +
            TIPO_DEBITO + " TEXT NOT NULL, " +
            ID_CONTA + "INTEGER NOT NULL, " +
            "FOREIGN KEY("+ID_CONTA+") " +"REFERENCES "+TABELA_CONTA+"("+ID_CONTA+"))";     */

    public SQLiteHelper(Context context) {
        super(context, NOME_DATABASE, null, VERSAO_DATABASE);
    }


    // Se o banco de dados ainda não existe então o método onCreate é chamado para criar o BD
    @Override
    public void onCreate(SQLiteDatabase database) {
        try{
            database.execSQL(CRIAR_TABELA_CONTA);
            database.execSQL(CRIAR_TABELA_CREDITO);
            database.execSQL(CRIAR_TABELA_DEBITO);
        }   catch(SQLException e){
                Log.d("ERRO_SQL", "Erro ao criar o BD.\n");
                e.getMessage();
                e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
