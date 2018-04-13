package com.glabexlabs.learn.crud;
import android.app.Dialog;
import android.content.*;
import android.os.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.*;
import android.widget.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.*;
import com.google.firebase.database.*;
import java.util.*;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.app.*;

/* 13.april.2018
* created by Hendriyawan
* thanks to https://www.twoh.co
*/

public class MainActivity extends AppCompatActivity implements AdapterBarangRecyclerView.FirebaseDataListener
{
	//variabel fields
	private Toolbar mToolbar;
	private FloatingActionButton mFloatingActionButton;
	private EditText mEditNama;
	private EditText mEditMerk;
	private EditText mEditHarga;
	private RecyclerView mRecyclerView;
	private AdapterBarangRecyclerView mAdapter;
	private ArrayList<Barang> daftarBarang;
	
	//variabel yang merefers ke Firebase Database
	private DatabaseReference mDatabaseReference;
	private FirebaseDatabase mFirebaseInstance;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		//android toolbar
		setupToolbar(R.id.toolbar);
		
		
		mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		
		FirebaseApp.initializeApp(this);
		// mengambil referensi ke Firebase Database
		mFirebaseInstance = FirebaseDatabase.getInstance();
		mDatabaseReference = mFirebaseInstance.getReference("barang");
		mDatabaseReference.child("data_barang").addValueEventListener(new ValueEventListener(){
			@Override
			public void onDataChange(DataSnapshot dataSnapshot){
				
				daftarBarang = new ArrayList<>();
				for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()){
					Barang barang = mDataSnapshot.getValue(Barang.class);
					barang.setKey(mDataSnapshot.getKey());
					daftarBarang.add(barang);
				}
				//set adapter RecyclerView
				mAdapter = new AdapterBarangRecyclerView(MainActivity.this, daftarBarang);
				mRecyclerView.setAdapter(mAdapter);
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError){
				// TODO: Implement this method
				Toast.makeText(MainActivity.this, databaseError.getDetails()+" "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
			}
				
		});
		
		
		//FAB (FloatingActionButton) tambah barang
		mFloatingActionButton = (FloatingActionButton)findViewById(R.id.tambah_barang);
		mFloatingActionButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//tambah barang
				dialogTambahBarang();
			}
		});
    }
	
	
	
	
	/* method ketika data di klik
	*/
	@Override
	public void onDataClick(final Barang barang, int position){
		//aksi ketika data di klik
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pilih Aksi");
		
		builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int id){
				dialogUpdateBarang(barang);
			}
		});
		builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int id){
				hapusDataBarang(barang);
			}
		});
		builder.setNeutralButton("BATAL", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int id){
				dialog.dismiss();
			}
		});
		
		Dialog dialog = builder.create();
		dialog.show();
	}
	
	
	
	//setup android toolbar
	private void setupToolbar(int id){
		mToolbar = (Toolbar)findViewById(id);
		setSupportActionBar(mToolbar);
	}



	//dialog tambah barang / alert dialog
	private void dialogTambahBarang(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tambah Data Barang");
		View view = getLayoutInflater().inflate(R.layout.layout_tambah_barang, null);

		mEditNama = (EditText)view.findViewById(R.id.nama_barang);
		mEditMerk = (EditText)view.findViewById(R.id.merk_barang);
		mEditHarga = (EditText)view.findViewById(R.id.harga_barang);

		builder.setView(view);

		//button simpan barang / submit barang
		builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int id){

					String namaBarang = mEditNama.getText().toString();
					String merkBarang = mEditMerk.getText().toString();
					String hargaBarang = mEditHarga.getText().toString();

					if(!namaBarang.isEmpty() && !merkBarang.isEmpty() && !hargaBarang.isEmpty()){
						submitDataBarang(new Barang(namaBarang, merkBarang, hargaBarang));
					}
					else {
						Toast.makeText(MainActivity.this, "Data harus di isi!", Toast.LENGTH_LONG).show();
					}
				}
			});

		//button kembali / batal
		builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int id){
					dialog.dismiss();
				}
			});
		Dialog dialog = builder.create();
		dialog.show();
	}



	//dialog update barang / update data barang
	private void dialogUpdateBarang(final Barang barang){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Edit Data Barang");
		View view = getLayoutInflater().inflate(R.layout.layout_edit_barang, null);
		
		mEditNama = (EditText)view.findViewById(R.id.nama_barang);
		mEditMerk = (EditText)view.findViewById(R.id.merk_barang);
		mEditHarga = (EditText)view.findViewById(R.id.harga_barang);

		mEditNama.setText(barang.getNama());
		mEditMerk.setText(barang.getMerk());
		mEditHarga.setText(barang.getHarga());
		builder.setView(view);
		
		//final Barang mBarang = (Barang)getIntent().getSerializableExtra("
		if (barang != null){
			builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int id){
					barang.setNama(mEditNama.getText().toString());
					barang.setMerk(mEditMerk.getText().toString());
					barang.setHarga(mEditHarga.getText().toString());
					updateDataBarang(barang);
				}
			});
		}
		builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int id){
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();

	}
	
	
	/**
	 * submit data barang
	 * ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
	 * set onSuccessListener yang berisi kode yang akan dijalankan
	 * ketika data berhasil ditambahkan
	 */
	private void submitDataBarang(Barang barang){
		mDatabaseReference.child("data_barang").push().setValue(barang).addOnSuccessListener(this, new OnSuccessListener<Void>(){
			@Override
			public void onSuccess(Void mVoid){
				Toast.makeText(MainActivity.this, "Data barang berhasil di simpan !", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/**
	 * update/edit data barang
	 * ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
	 * set onSuccessListener yang berisi kode yang akan dijalankan
	 * ketika data berhasil ditambahkan
	 */
	private void updateDataBarang(Barang barang){
		mDatabaseReference.child("data_barang").child(barang.getKey()).setValue(barang).addOnSuccessListener(new OnSuccessListener<Void>(){
			@Override
			public void onSuccess(Void mVoid){
				Toast.makeText(MainActivity.this, "Data berhasil di update !", Toast.LENGTH_LONG).show();
			}
		});
	}
	/**
	 * hapus data barang
	 * ini kode yang digunakan untuk menghapus data yang ada di Firebase Realtime Database
	 * set onSuccessListener yang berisi kode yang akan dijalankan
	 * ketika data berhasil dihapus
	 */
	private void hapusDataBarang(Barang barang){
		if(mDatabaseReference != null){
			mDatabaseReference.child("data_barang").child(barang.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>(){
				@Override
				public void onSuccess(Void mVoid){
					Toast.makeText(MainActivity.this,"Data berhasil di hapus !", Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
