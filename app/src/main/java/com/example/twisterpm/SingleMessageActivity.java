package com.example.twisterpm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleMessageActivity extends AppCompatActivity {
    public static final String MESSAGE = "MESSAGE";
    private Message originalMessage;
    private Comment originalComment;
    private TextView messageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_message);
        messageView = findViewById(R.id.SingleMessageTextView);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        originalMessage = (Message) intent.getSerializableExtra(MESSAGE);
        getAndShowComment();

        Log.d("Apple", "New message: " + originalMessage.toString() + ", With ID: " + originalMessage.getId());
        messageView.setText(originalMessage.getContent());
    }


    //Show Comments
    private void getAndShowComment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TwisterService service = retrofit.create(TwisterService.class);
        Call<List<Comment>> getAllComments = service.getComments(originalMessage.getId());

        getAllComments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
//                List<Comment> comments = response.body();
//                showIt(comments);

                if (response.isSuccessful()) {
                    List<Comment> allComments = response.body();
                    populateRecyclerView(allComments);
                } else {
                    String message = "Problem" + response.code() + " " + response.message();
                    Log.d("Apples", message);
                    messageView.setText((message));
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.d("Apple", t.getMessage());

            }
        });

    }

    //Show comments
    private void showIt(List<Comment> comments) {
        Log.d("Apple", "Comments" + comments.toString());
        RecyclerView recyclerView = findViewById(R.id.commentRecyclerView);
        RecyclerViewSimpleAdapter<Comment> adapter = new RecyclerViewSimpleAdapter<>(comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
    }

    private void populateRecyclerView(List<Comment> allComments) {
        RecyclerView recyclerView = findViewById(R.id.commentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewSimpleAdapter<Comment> adapter = new RecyclerViewSimpleAdapter<Comment>(allComments) {
        };
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position, element) -> {
            Comment comment = (Comment) element;
            Log.d("Apple", element.toString());
            Intent intent = new Intent(SingleMessageActivity.this, SingleCommentAvtivity.class);
            intent.putExtra(SingleCommentAvtivity.COMMENT, comment);
            intent.putExtra(SingleMessageActivity.MESSAGE, originalMessage);

            startActivity(intent);
        });
    }


    public void pickComment(View view) {
        EditText contenField = findViewById(R.id.mainMessageEditTextSendMessage);
        String content = contenField.getText().toString();
        Message comment = new Message();
        comment.setContent(content);
        comment.setUser("Nicolai");


    }

    //Go Back
    public void GoBack(View view) {
        Log.d("Apple", "Go back to MainMessage");
        finish();
    }

    //Add Comment
    public void addCommentClick(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Comment comment = new Comment();
        EditText contentField = findViewById(R.id.SingleMessageEditCommentText);
        String content = contentField.getText().toString().trim();
        comment.setContent(content);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        comment.setUser(currentUser.getEmail());
        int messageId = originalMessage.getId();
        Log.d("Apple", "Add comment: originalMessageId: " + messageId);
        comment.setMessageId(messageId);
        Log.d("Apple", "Add comment: comment.GetMessageID: " + comment.getMessageId());
        TwisterService service = retrofit.create(TwisterService.class);
        Call<Comment> saveComment = service.postComment(originalMessage.getId(), comment);

        saveComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment newComment = response.body();
                    Log.d("Apple", newComment.toString());
                    Toast.makeText(SingleMessageActivity.this, newComment.getContent(), Toast.LENGTH_SHORT).show();
                } else {
                    String problem = "Problem:" + " " + response.code() + " " + response.message();
                    Log.d("Apple", problem);
                    messageView.setText("Problem");
                }

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                messageView.setText(t.getMessage());
                Log.e("Apple", t.getMessage());

            }
        });
    }

    //Delete Message
    public void deleteMessageClick(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("Apple", currentUser.getEmail());
        Log.d("Apple", "original message" + originalMessage.getUser());
        if (!currentUser.getEmail().equals(originalMessage.getUser())) {
            Toast.makeText(getBaseContext(), "Cannot delete others message ", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int messageId = originalMessage.getId();
        TwisterService service = retrofit.create(TwisterService.class);
        Call<Message> deleteMessage = service.deleteMessage(messageId);

        deleteMessage.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Log.d("Apple", "does it work");
                    finish();

                } else {
                    String problem = call.request().url() + "\n" + response.code() + " " + response.message();
                    Log.e("Apple", problem);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("Apple", "Problem: " + t.getMessage());

            }
        });
    }

    //Delete Comment
    public void deleteCommentClick(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        int commentId = originalComment.getId();
        TwisterService service = retrofit.create(TwisterService.class);
        Call<Comment> deleteComment = service.deleteComment(originalMessage.getId(), commentId);

        deleteComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    String comment = "Comment deleted" + " " + originalComment.getId();
                    Toast.makeText(getBaseContext(), comment, Toast.LENGTH_SHORT).show();
                    Log.d("Apple", comment);
                } else {
                    String problem = call.request().url() + "\n" + response.code() + " " + response.message();
                    Log.e("Apple", problem);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("Apple", "Problem: " + t.getMessage());
            }
        });

    }
}