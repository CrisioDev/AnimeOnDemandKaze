package de.a_b_software.anime_on_demand_kaze;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "de.a-b-software.anime_on_demand_kaze.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);

        String unm=sp1.getString("Unm", null);
        String pass = sp1.getString("Psw", null);
        EditText userT = (EditText) findViewById(R.id.userText);
        userT.setText(unm);
        EditText passT = (EditText) findViewById(R.id.passwordText);
        passT.setText(pass);
        Button lgn = (Button) findViewById(R.id.button);
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userT = (EditText) findViewById(R.id.userText);
                String user = userT.getText().toString();
                EditText passT = (EditText) findViewById(R.id.passwordText);
                String password = passT.getText().toString();
                SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("Unm",user);
                Ed.putString("Psw",password);
                Ed.apply();

                TextView show = (TextView) findViewById(R.id.textView);

                getWebsite(show, user, password);
            }
        });
    }


    /*
    *
    *
    *
    * <form class="form" action="/users/sign_in" accept-charset="UTF-8" method="post">
    *     <input name="utf8" type="hidden" value="✓">
    *     <input type="hidden" name="authenticity_token" value="aqfywxA3Qr/TsTxkspMR8q6/hCo10OoKnBZ2a5pNh2HoZmRrStmbXnLzHvIztlhkKjUbhUptS5E32LJmA4lhvQ==">
          <input autofocus="autofocus" class="form-control" type="text" name="user[login]" id="user_login">
          <input class="form-control" type="password" name="user[password]" id="user_password">
          <input name="user[remember_me]" type="hidden" value="0">
        </form>
    *
    *
    * */

    private void getWebsite(final TextView show, final String user, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document aod = Jsoup.connect("https://www.anime-on-demand.de/").get();
                    Elements forms = aod.getElementsByClass("form");
                    List<String> values = forms.first().getElementsByTag("input").eachAttr("value");
                    String action = forms.first().attr("action");
                    String utf8 = values.get(0);
                    String token = values.get(1);
                    String rememberme = values.get(2);
                    String remembermetoo = values.get(3);
                    String commit = values.get(4);
                    System.out.println(token);
                    builder.append(token);


                    Document login = Jsoup.connect("https://www.anime-on-demand.de/users/sign_in")
                            .data("utf8",utf8)
                            .data("authenticity_token",token)
                            .data("user[login]",user)
                            .data("user[password]",password)
                            .data("user[remember_me]",rememberme)
                            .data("commit",commit)
                            .followRedirects(true)
                            .post();

                    Document aodloggedin = Jsoup.connect("https://www.anime-on-demand.de/").get();

                    builder.append(aodloggedin.data());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        show.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}
