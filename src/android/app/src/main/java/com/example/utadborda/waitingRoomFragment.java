package com.example.utadborda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class waitingRoomFragment extends Fragment {

    ListView listView;

    String playerName = "";
    String sessionKey= "";
    Long playerCount;
    List<String> userList;

    FirebaseDatabase database;
    DatabaseReference sessionRef;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiting_room, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        //playerName = preferences.getString("playerName", "");

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            playerName = extras.getString("playerName");
            playerCount = extras.getLong("playerCount");
            sessionKey = extras.getString("sessionName");
            sessionRef = database.getReference("sessions/"+ sessionKey + "/player-" + playerCount);
            sessionRef.setValue(playerName);
        }

        addRoomsEventListener();
    }

    private void addRoomsEventListener() {
        sessionRef = database.getReference("sessions/" + sessionKey);
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //show list of users
                userList.clear();
                Iterable<DataSnapshot> users = snapshot.getChildren();
                for(DataSnapshot dataSnapshot : users) {
                    userList.add(dataSnapshot.getKey());

                    ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error - nothing

            }
        });
    }
}