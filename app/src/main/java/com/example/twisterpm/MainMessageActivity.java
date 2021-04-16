package com.example.twisterpm;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Tag;

public class MainMessageActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String LOG_TAG = "MINE";
    private TextView messageView;
    private GestureDetector gestureDetector;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);
        gestureDetector = new GestureDetector(this, this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        messageView = findViewById(R.id.MainActicityTextView);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAndShowData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_Login:
                Intent intentLogin = new Intent(this, MainActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.action_Messages:
                Intent intentMessages = new Intent(this, MainMessageActivity.class);
                startActivity(intentMessages);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Showing the messages
    private void getAndShowData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwisterService service = retrofit.create(TwisterService.class);
        Call<List<Message>> getAllMessages = service.getMessages();

        getAllMessages.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                Log.d("Apple", response.body().toString());

                if (response.isSuccessful()) {
                    List<Message> allMessages = response.body();
                    populateRecyclerView(allMessages);
                } else {
                    String message = "Problem" + response.code() + " " + response.message();
                    Log.d("Apples", message);
                    messageView.setText((message));
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.d("Apple", t.getMessage());
            }
        });
    }

    //RecyclerView
    private void populateRecyclerView(List<Message> allMessages) {
        RecyclerView recyclerView = findViewById(R.id.MainMessageActivityRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewSimpleAdapter<Message> adapter = new RecyclerViewSimpleAdapter<Message>(allMessages) {
        };

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, element) -> {
            Message message = (Message) element;
            Log.d("Apple", element.toString());
            Intent intent = new Intent(MainMessageActivity.this, SingleMessageActivity.class);
            intent.putExtra(SingleMessageActivity.MESSAGE, message);
            startActivity(intent);
        });
    }

    public void writeAMessage(View view) {
        EditText contenField = findViewById(R.id.mainMessageEditTextSendMessage);
        String content = contenField.getText().toString();
        Message message = new Message();
        message.setContent(content);
        message.setUser("Nicolai");
    }

    //Add Message
    public void addMessageButton(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Message message = new Message();
        EditText contentField = findViewById(R.id.mainMessageEditTextSendMessage);
        String content = contentField.getText().toString().trim();
        message.setContent(content);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        message.setUser(currentUser.getEmail());
        TwisterService service = retrofit.create(TwisterService.class);
        Call<Message> saveMessage = service.postMessage(message);

        saveMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Message newMessage = response.body();
                    Log.d("Apple", newMessage.toString());
                    Toast.makeText(MainMessageActivity.this, newMessage.getContent(), Toast.LENGTH_SHORT).show();
                } else {
                    String problem = "Problem:" + " " + response.code() + " " + response.message();
                    Log.d("Apple", problem);
                    messageView.setText("Problem");
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                messageView.setText(t.getMessage());
                Log.e("Apple", t.getMessage());
            }
        });

    }

    public void LogOutClick(View view) {
        Log.d("Apple", "LogOut Click");
        mAuth.signOut();
        finish();
    }

    //Gestures

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Apple", "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Aplle", "onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Apple", "onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Apple", "onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Apple", "onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Apple", "onFling " + e1.toString() + " " + e2.toString());

        Boolean leftSwipe = e1.getX() > e2.getX();
        Log.d("Apple", "onFling " + leftSwipe);
        if (leftSwipe) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}