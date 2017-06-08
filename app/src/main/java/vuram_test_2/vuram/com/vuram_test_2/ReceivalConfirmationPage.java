package vuram_test_2.vuram.com.vuram_test_2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.WelcomeButtonAction;

public class ReceivalConfirmationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_receival_card_details);
      /*  Card card = new Card.Builder(this)
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_welcome_card_layout)
                .setTitle("Welcome Card")
                .setTitleColor(Color.WHITE)
                .setDescription("I am the description")
                .setDescriptionColor(Color.WHITE)
                .setSubtitle("My subtitle!")
                .setSubtitleColor(Color.WHITE)
                .setBackgroundColor(Color.BLUE)
                .addAction(R.id.ok_button, new WelcomeButtonAction(this)
                        .setText("Okay!")
                        .setTextColor(Color.WHITE)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                            }
                        }))
                .endConfig()
                .build();*/

    }
}
