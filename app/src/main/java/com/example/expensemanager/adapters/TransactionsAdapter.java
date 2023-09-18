package com.example.expensemanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowTransactionBinding;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{
   Context context;
   RealmResults<Transaction> transactions;

   public TransactionsAdapter(Context context, RealmResults<Transaction> transactions)
   {
       this.context = context;
       this.transactions = transactions;
   }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
           Transaction transaction = transactions.get(position);

           holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
           holder.binding.accountLbl.setText(transaction.getAccount());
           holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
           holder.binding.transactionCategory.setText(transaction.getCategory());

           Category transactionCategory = Constants.getCategoryDetail(transaction.getCategory());
           holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
           holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

           holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountColor(transaction.getAccount())));
           if(transaction.getType().equals(Constants.INCOME)){
               holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor));
           }else if(transaction.getType().equals(Constants.EXPENSE)){
               holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor));
           }

           holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                   deleteDialog.setTitle("Delete Transaction");
                   deleteDialog.setMessage("Are you sure want to delete this transaction?");
                   deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                       ((MainActivity)context).viewModel.deleteTransaction(transaction);
                   });
                   deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", (dialog, which) -> {
                       deleteDialog.dismiss();
                   });
                   deleteDialog.show();
                   return false;
               }
           });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
