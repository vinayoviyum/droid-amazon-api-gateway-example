package com.oviyum.androidamazonawsexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.regions.Regions;
import com.oviyum.awsexample.FleetMobileClient;
import com.oviyum.awsexample.model.Empty;
import com.oviyum.awsexample.model.LoginRequest;
import com.oviyum.awsexample.model.LoginResponse;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements TaskListner,View.OnClickListener{

    private ApiClientFactory apiClientFactory;
    private FleetMobileClient fleetfootClient;

    private Button login_btn;
    private EditText username_et,password_et;
    private TextView reponse_tv;

    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = (Button)findViewById(R.id.login_btn);
        username_et = (EditText)findViewById(R.id.username_et);
        password_et = (EditText)findViewById(R.id.password_et);
        reponse_tv = (TextView)findViewById(R.id.response_tv);

        AWSCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                this,          // activity context
                "us-east-1:a5cf40c8-c0a1-40ab-bd23-5d456c3fbc2e", // Cognito identity pool id
                Regions.US_EAST_1 // region of Cognito identity pool
        );




        apiClientFactory = new ApiClientFactory();
        fleetfootClient = apiClientFactory.build(FleetMobileClient.class);

        login_btn.setOnClickListener(this);

    }

    @Override
    public void onListned(String reponse) {
        reponse_tv.setText(reponse);
    }

    @Override
    public void onClick(View view) {
        BackgroundTask backgroundTask = new BackgroundTask(this,fleetfootClient);
        backgroundTask.execute();
    }

    private class BackgroundTask extends AsyncTask<String,String,String> {

        private TaskListner taskListner;
        private FleetMobileClient fleetfootClient;

        private LoginResponse loginModel;

        private LoginRequest loginRequest;

        StringBuilder stringBuilder;


        public BackgroundTask(TaskListner taskListner, FleetMobileClient fleetfootClient){
            this.taskListner = taskListner;
            this.fleetfootClient = fleetfootClient;
            this.loginRequest = new LoginRequest();
            stringBuilder = new StringBuilder();
        }


        @Override
        protected String doInBackground(String... strings) {

            loginRequest.setEmail("admin@shary.com");
            loginRequest.setPassword("spravce9631");


             loginModel = fleetfootClient.processLoginPost(loginRequest);




            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            stringBuilder.append("credentials"+loginModel.getCredentials());
            stringBuilder.append("Identity ID"+loginModel.getIdentityId());
            stringBuilder.append("IsMaster"+loginModel.getIsMaster());
            stringBuilder.append("Locations"+loginModel.getLocations());
            stringBuilder.append("Owners"+loginModel.getOwners());
            stringBuilder.append("Locations"+loginModel.getLocations());
            stringBuilder.append("Status"+loginModel.getStatus());
            stringBuilder.append("Token"+loginModel.getToken());

            String s1;

           s1 = stringBuilder.substring(0,stringBuilder.capacity());

            taskListner.onListned(s1);
            super.onPostExecute(s);
        }
    }
}
