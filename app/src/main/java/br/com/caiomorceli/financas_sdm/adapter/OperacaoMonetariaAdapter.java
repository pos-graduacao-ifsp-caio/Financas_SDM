package br.com.caiomorceli.financas_sdm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.com.caiomorceli.financas_sdm.R;
import br.com.caiomorceli.financas_sdm.model.OperacaoMonetaria;

public class OperacaoMonetariaAdapter extends RecyclerView.Adapter<OperacaoMonetariaAdapter.OperacaoMonetariaViewHolder>   {

    private List<OperacaoMonetaria> operacoesMonetarias;
    private Context context;

    private static ItemClickListener clickListener;

    public OperacaoMonetariaAdapter(List<OperacaoMonetaria> operacoesMonetarias, Context context) {
        this.operacoesMonetarias = operacoesMonetarias;
        this.context = context;
    }

    // transforma o arquivo xml em uma view na activity.
    @Override
    public OperacaoMonetariaViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.celula_operacao_monetaria, parent, false);
        return new OperacaoMonetariaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperacaoMonetariaViewHolder operacaoMonetariaViewHolder, int position) {
        OperacaoMonetaria operacaoMonetaria  = operacoesMonetarias.get(position) ;
        operacaoMonetariaViewHolder.descricaoOperacao.setText(operacaoMonetaria.getDescricao());
        operacaoMonetariaViewHolder.valorOperacao.setText("R$ "+operacaoMonetaria.getValor());
    }

    @Override
    public int getItemCount() {
        return operacoesMonetarias.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class OperacaoMonetariaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView descricaoOperacao;
        final TextView valorOperacao;

        OperacaoMonetariaViewHolder(View view) {
            super(view);
            descricaoOperacao = view.findViewById(R.id.text_view_descricao_operacao);
            valorOperacao = view.findViewById(R.id.text_view_valor_operacao);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                // passa como parâmetro a posicao do item da recyclerView, o id do componente que foi clicado e a propria referência da view.
                clickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    // passa como parâmetro a posicao do item da recyclerView e o id do componente que foi clicado.
    public interface ItemClickListener {
        void onItemClick(int position);
    }

}

