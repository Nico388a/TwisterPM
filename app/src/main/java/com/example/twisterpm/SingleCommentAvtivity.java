package com.example.twisterpm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SingleCommentAvtivity extends AppCompatActivity {
    public static final String COMMENT = "COMMENT";
    private Comment originalComment;
    private TextView commentView;
    private Message originalMessage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_comment_avtivity);
        commentView = findViewById(R.id.singleCommentTextView);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        originalComment = (Comment) intent.getSerializableExtra(COMMENT);
        originalMessage = (Message) intent.getSerializableExtra(SingleMessageActivity.MESSAGE);
        Log.d("Apple", "New comment: " + originalComment.toString() + ", with ID: " + originalComment.getId());
        commentView.setText(originalComment.getContent());
    }

    //Delete Comment
    public void deleteCommentClick(View view) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("Apple", currentUser.getEmail());
        Log.d("Apple", "original comment" + originalComment.getUser());
        if (!currentUser.getEmail().equals(originalComment.getUser())) {
            Toast.makeText(getBaseContext(), "Cannot delete others Comments ", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://anbo-restmessages.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        int commentId = originalComment.getId();
        TwisterService service = retrofit.create(TwisterService.class);
        int messageId = originalMessage.getId();
        Log.d("Apple", "Delete Comment: Message ID: " + messageId);
        Log.d("Apple", "Delete Comment: Comment ID: " + commentId);
        Call<Comment> deleteComment = service.deleteComment(messageId, commentId);

        deleteComment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d("Apple", "Response is received.");
                if (response.isSuccessful()) {
                    Log.d("Apple", "Response is success.");
                    finish();
//                    String comment = "Comment deleted" + " " + originalComment.getId();
//                    Toast.makeText(getBaseContext(), comment, Toast.LENGTH_SHORT).show();
//                    Log.d("Apple", comment);
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

    public void GoBackToMessages(View view) {
        Log.d("Apple", "Going back to messages");
        finish();
    }

    public void goToDoNothing(View view) {
        Intent intentLogin = new Intent(SingleCommentAvtivity.this, DoNothingActivity.class);
        startActivity(intentLogin);
    }
}