package com.example.zakatgold;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etGoldWeight, etGoldPrice;
    RadioGroup radioGroupType;
    RadioButton rbKeep, rbWear;
    Button btnCalculate, btnReset;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Zakat Gold Calculator");
        }

        // Inputs
        etGoldWeight = findViewById(R.id.etGoldWeight);
        etGoldPrice = findViewById(R.id.etGoldPrice);
        radioGroupType = findViewById(R.id.radioGroupType);
        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvResult = findViewById(R.id.tvResult);

        btnCalculate.setOnClickListener(v -> calculateZakat());
        btnReset.setOnClickListener(v -> resetFields());
    }

    private void calculateZakat() {

        String weightInput = etGoldWeight.getText().toString().trim();
        String priceInput = etGoldPrice.getText().toString().trim();

        // Validation: Empty fields
        if (weightInput.isEmpty()) {
            etGoldWeight.setError("Please enter gold weight");
            toast("Gold weight is required.");
            return;
        }

        if (priceInput.isEmpty()) {
            etGoldPrice.setError("Please enter gold price per gram");
            toast("Gold price per gram is required.");
            return;
        }

        double weight = Double.parseDouble(weightInput);
        double price = Double.parseDouble(priceInput);

        // Validation: Invalid numbers
        if (weight <= 0) {
            etGoldWeight.setError("Weight must be greater than 0");
            toast("Gold weight must be more than 0.");
            return;
        }

        if (price <= 0) {
            etGoldPrice.setError("Price must be greater than 0");
            toast("Gold price must be more than 0.");
            return;
        }

        // Uruf logic
        double uruf;
        String type;

        if (rbKeep.isChecked()) {
            uruf = 85;
            type = "Keep";
        } else {
            uruf = 200;
            type = "Wear";
        }

        double excess = weight - uruf;
        if (excess < 0) excess = 0;

        double totalGoldValue = weight * price;
        double zakatPayableValue = excess * price;
        double zakat = zakatPayableValue * 0.025;

        // Final result (formatted)
        String result =
                "Gold Type: " + type +
                        "\nTotal Gold Weight: " + weight + " g" +
                        "\nUruf (Threshold): " + uruf + " g" +
                        "\nGold Weight After Uruf: " + excess + " g" +
                        "\n\n💰 Total Gold Value: RM " + String.format("%.2f", totalGoldValue) +
                        "\n💰 Zakat Payable Value: RM " + String.format("%.2f", zakatPayableValue) +
                        "\n💰 Total Zakat (2.5%): RM " + String.format("%.2f", zakat);


        tvResult.setText(result);

        // Fade + Slide Animation
        tvResult.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_slide)
        );

        toast("Zakat calculation completed.");
    }

    private void resetFields() {
        etGoldWeight.setText("");
        etGoldPrice.setText("");
        rbKeep.setChecked(true);
        tvResult.setText("Result will appear here");
        toast("All fields cleared.");
    }

    // Toolbar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_share) {
            shareApp();
            return true;
        }
        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out my Zakat Gold Calculator App!\nGitHub Link: https://github.com/nmsyahir-it/ZakatGoldCalculator.git");

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    // Toast helper
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
