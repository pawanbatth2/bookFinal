package com.example.book.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.BooksAdapter;
import com.example.book.Models.Books;
import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.paperdb.Book;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    DatabaseReference databaseReference;
    ArrayList<Books>arrayList;
    BooksAdapter booksAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        recyclerView=root.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        retreivelistgroups();
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void retreivelistgroups() {
        arrayList=new ArrayList<>();

        databaseReference=  FirebaseDatabase.getInstance().getReference().child("Books");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    Books newData;
                    arrayList.add(newData = new Books( snapshot.child("bTitle").getValue(String.class),snapshot.child("detail").getValue(String.class),snapshot.child("price").getValue(String.class),snapshot.child("image").getValue(String.class)
                            ,snapshot.child("product").getValue(String.class),snapshot.child("pid").getValue(String.class)
                            ,snapshot.child("date").getValue(String.class),snapshot.child("time").getValue(String.class)
                             ));
                }
                Collections.sort(arrayList, new Comparator<Books>() {
                    @Override
                    public int compare(Books o1, Books o2) {
                        return 0;
                    }
                });

                booksAdapter=new BooksAdapter(getActivity(),arrayList);
                recyclerView.setAdapter(booksAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
