package br.com.caiomorceli.financas_sdm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.caiomorceli.financas_sdm.R;
import br.com.caiomorceli.financas_sdm.model.Conta;

public class ContaAdapter extends RecyclerView.Adapter<ContaAdapter.ContaViewHolder>  {

    private List<Conta> contas;
    private Context context;

    private static ItemClickListener clickListener;

    public ContaAdapter(List<Conta> contas, Context context) {
        this.contas = contas;
        this.context = context;
    }

    // transforma o arquivo xml em uma view na activity.
    @Override
    public ContaViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.celula_conta, parent, false);
        return new ContaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContaViewHolder contaViewHolder, int position) {
        Conta conta  = contas.get(position) ;
        contaViewHolder.descricaoConta.setText(conta.getDescricao());
    }

    @Override
    public int getItemCount() {
        return contas.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class ContaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView descricaoConta;

        ContaViewHolder(View view) {
            super(view);
            descricaoConta = view.findViewById(R.id.descricao_conta);

            descricaoConta.setOnClickListener(this);
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
