package de.a_b_software.anime_on_demand_kaze;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

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

                getWebsite(user, password);
            }
        });

    }

    private void showGrid(String[][] titleArray,Map<String,String> cookie){
        Intent myanimesintent = new Intent(this, MyAnimes.class);
        myanimesintent.putExtra("EXTRA_TITLE_LIST",titleArray[0]);
        myanimesintent.putExtra("EXTRA_LINK_LIST",titleArray[1]);
        myanimesintent.putExtra("EXTRA_PIC_LIST",titleArray[2]);
        myanimesintent.putExtra("EXTRA_COOKIE", (Serializable) cookie);
        startActivity(myanimesintent);
    }

    private void getWebsite(final String user, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    //Document aod = Jsoup.connect("https://www.anime-on-demand.de/").get();
                    Connection.Response loginCon = Jsoup.connect("https://www.anime-on-demand.de/").method(Connection.Method.GET).execute();
                    Document aod = loginCon.parse();
                    Elements forms = aod.getElementsByClass("form");
                    List<String> values = forms.first().getElementsByTag("input").eachAttr("value");
                    String action = forms.first().attr("action");
                    String utf8 = values.get(0);
                    String token = values.get(1);
                    String rememberme = values.get(2);
                    String remembermetoo = values.get(3);
                    String commit = values.get(4);
                    /*builder.append(utf8 + " ");
                    builder.append(token + " ");
                    builder.append(rememberme + " ");
                    builder.append(user + " ");
                    builder.append(password + " ");
                    builder.append(commit + " ");

                    System.out.println("UTF8 " + utf8);
                    System.out.println("TOKEN " + token);
                    System.out.println("USER " + user);
                    System.out.println("PASSWORD " + password);
                    System.out.println("COMMIT " + commit);*/

                    /*Document login = Jsoup.connect("https://www.anime-on-demand.de/users/sign_in")
                            .data("utf8",utf8)
                            .data("authenticity_token",token)
                            .data("user[login]",user)
                            .data("user[password]",password)
                            .data("user[remember_me]","0")
                            .data("commit","Einloggen")
                            .cookies(loginCon.cookies())
                            .followRedirects(true)
                            .post();*/

                    Connection.Response login = Jsoup.connect("https://www.anime-on-demand.de/users/sign_in")
                            .data("utf8",utf8)
                            .data("authenticity_token",token)
                            .data("user[login]",user)
                            .data("user[password]",password)
                            .data("user[remember_me]","0")
                            .data("commit","Einloggen")
                            .cookies(loginCon.cookies())
                            .followRedirects(true)
                            .method(Connection.Method.POST)
                            .execute();

                    Document myanimes = Jsoup.connect("https://www.anime-on-demand.de/myanimes").cookies(login.cookies()).get();
                    Elements myanime = myanimes.getElementsByClass("three-box animebox");
                    String[][] titleArray = new String[3][myanime.size()];
                    for(int i=0; i < myanime.size(); i++){
                        titleArray[0][i] = myanime.get(i).getElementsByClass("animebox-title").first().text();
                        titleArray[1][i] = myanime.get(i).getElementsByClass("animebox-link").first().getElementsByTag("a").first().attr("href");
                        titleArray[2][i] = myanime.get(i).getElementsByClass("animebox-image").first().getElementsByTag("img").first().attr("src");
                    }
                    showGrid(titleArray,login.cookies());

                } catch (IOException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }
}
