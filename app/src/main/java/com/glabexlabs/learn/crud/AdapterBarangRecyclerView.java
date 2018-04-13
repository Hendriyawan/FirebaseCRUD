package com.glabexlabs.learn.crud;

import android.content.*;
import android.support.v7.widget.*;
import android.view.*;
import java.util.*;
import android.support.v7.app.*;

public class AdapterBarangRecyclerView extends RecyclerView.Adapter<ItemBarangViewHolder>
{
	private Context context;
	private ArrayList<Barang> daftarBarang;
	private FirebaseDataListener listener;
	
	public AdapterBarangRecyclerView(Context context, ArrayList<Barang> daftarBarang){
		this.context = context;
		this.daftarBarang = daftarBarang;
		this.listener = (FirebaseDataListener)context;
	}

	@Override
	public ItemBarangViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		// TODO: Implement this method
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
		ItemBarangViewHolder holder = new ItemBarangViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(ItemBarangViewHolder holder, final int position)
	{
		// TODO: Implement this method
		holder.namaBarang.setText("Nama   : "+daftarBarang.get(position).getNama());
		holder.merkBarang.setText("Merk     : "+daftarBarang.get(position).getMerk());
		holder.hargaBarang.setText("Harga   : "+daftarBarang.get(position).getHarga());
		
		holder.view.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				listener.onDataClick(daftarBarang.get(position), position);
			}
		});
	}

	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return daftarBarang.size();
	}
	
	
	//interface data listener
	public interface FirebaseDataListener {
		void onDataClick(Barang barang, int position);
	}
}
