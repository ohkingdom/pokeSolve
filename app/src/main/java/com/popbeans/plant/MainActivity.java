package com.popbeans.plant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.penfeizhou.animation.apng.APNGDrawable;
import com.github.penfeizhou.animation.loader.ResourceStreamLoader;

public class MainActivity extends AppCompatActivity {

    private Pokemon pokemon;
    private TextView pokemonName;
    private ImageView pokemonSprite;
    int avatarDraw = R.drawable.spr_5b_151;
    private TextView pokemonLevel;
    private String activeButtonContext;

    private MathGame game;

    private TextView statSunValCur;
    private TextView statSunValMax;
    private ProgressBar progressBarSun;

    private TextView statWaterValCur;
    private TextView statWaterValMax;
    private ProgressBar progressBarWater;

    private TextView statLoveValCur;
    private TextView statLoveValMax;
    private ProgressBar progressBarLove;

    // Call to redraw UI elements

    @SuppressLint("SetTextI18n")
    public void drawDetails() {

        // Draw Avatar and Details

        switch (pokemon.getPokemonLevel()) {
            case 1 :
                if (pokemon.getPokemonType().equals("fire")) {
                    avatarDraw = R.drawable.spr_5b_004;
                }
                if (pokemon.getPokemonType().equals("grass")) {
                    avatarDraw = R.drawable.spr_5b_001;
                }
                if (pokemon.getPokemonType().equals("water")) {
                    avatarDraw = R.drawable.spr_5b_007;
                }
                break;
            case 2 :
                if (pokemon.getPokemonType().equals("fire")) {
                    avatarDraw = R.drawable.spr_5b_005;
                }
                if (pokemon.getPokemonType().equals("grass")) {
                    avatarDraw = R.drawable.spr_5b_002;
                }
                if (pokemon.getPokemonType().equals("water")) {
                    avatarDraw = R.drawable.spr_5b_008;
                }
                break;
            case 3 :
                if (pokemon.getPokemonType().equals("fire")) {
                    avatarDraw = R.drawable.spr_5b_006;
                }
                if (pokemon.getPokemonType().equals("grass")) {
                    avatarDraw = R.drawable.spr_5b_003_m;
                }
                if (pokemon.getPokemonType().equals("water")) {
                    avatarDraw = R.drawable.spr_5b_009;
                }
                break;
        }
        ResourceStreamLoader resourceLoader = new ResourceStreamLoader(MainActivity.this, avatarDraw);
        APNGDrawable apngDrawable = new APNGDrawable((resourceLoader));
        pokemonSprite.setImageDrawable(apngDrawable);

        pokemonName.setText(pokemon.getPokemonName());
        pokemonLevel.setText("Level. " + pokemon.getPokemonLevel());
    }

    @SuppressLint("SetTextI18n")
    public void drawStatistics() {

        // Draw Statistic Values
        statSunValCur.setText(Integer.toString(pokemon.getValCur("sun")));
        statSunValMax.setText(Integer.toString(pokemon.getValMax("sun")));
        statWaterValCur.setText(Integer.toString(pokemon.getValCur("water")));
        statWaterValMax.setText(Integer.toString(pokemon.getValMax("water")));
        statLoveValCur.setText(Integer.toString(pokemon.getValCur("love")));
        statLoveValMax.setText(Integer.toString(pokemon.getValMax("love")));

        // Draw Progress Bars
        progressBarSun.setProgress(pokemon.getValCur("sun"));
        progressBarSun.setMax(pokemon.getValMax("sun"));
        progressBarWater.setProgress(pokemon.getValCur("water"));
        progressBarWater.setMax(pokemon.getValMax("water"));
        progressBarLove.setProgress(pokemon.getValCur("love"));
        progressBarLove.setMax(pokemon.getValMax("love"));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pokemon = new Pokemon(extras.getString("Type"), extras.getString("Name"));
        } else {
            pokemon = new Pokemon(null, null);
        }

        pokemon = new Pokemon(extras.getString("Type"), extras.getString("Name"));
        pokemonName = findViewById(R.id.pokemonName);
        pokemonName.setText(pokemon.getPokemonName());
        pokemonSprite = findViewById(R.id.pokemonAvatar);
        pokemonLevel = findViewById(R.id.pokemonLevel);

        game = new MathGame();
        game.setDifficulty(1);

        statSunValCur = findViewById(R.id.statSunValCur);
        statSunValMax = findViewById(R.id.statSunValMax);
        statWaterValCur = findViewById(R.id.statWaterValCur);
        statWaterValMax = findViewById(R.id.statWaterValMax);
        statLoveValCur = findViewById(R.id.statLoveValCur);
        statLoveValMax = findViewById(R.id.statLoveValMax);

        progressBarSun = findViewById(R.id.progressBarSun);
        progressBarWater = findViewById(R.id.progressBarWater);
        progressBarLove = findViewById(R.id.progressBarLove);

        Button btnSun = findViewById(R.id.btnSun);
        Button btnWater = findViewById(R.id.btnWater);
        Button btnLove = findViewById(R.id.btnLove);
        ImageButton btnEvolve = findViewById(R.id.btnEvolve);

        drawDetails();
        drawStatistics();

        btnSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeButtonContext = "sun";
                mathDialog(savedInstanceState).show();
            }
        });

        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeButtonContext = "water";
                mathDialog(savedInstanceState).show();
            }
        });

        btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeButtonContext = "love";
                mathDialog(savedInstanceState).show();
            }
        });

        btnEvolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toastMessage;
                if (pokemon.evolve()) {
                    drawDetails();
                    drawStatistics();
                    game.setDifficulty(pokemon.getPokemonLevel());
                    toastMessage = "Evolution Successful!";

                } else {
                    toastMessage = "Evolution Failed!";
                }
                Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    public Dialog mathDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = (inflater.inflate(R.layout.math_dialog, null));
        builder.setView(dialogView);
        builder.setTitle(R.string.header_math_modal_text);
        TextView constantTextView = (TextView) dialogView.findViewById(R.id.constantTextView);
        constantTextView.setText(game.getConstant());
        TextView operatorTextView = (TextView) dialogView.findViewById(R.id.operatorTextView);
        operatorTextView.setText(game.getOperator());
        TextView sumTextView = (TextView) dialogView.findViewById(R.id.sumTextView);
        sumTextView.setText(game.getSum());
        final EditText variableEditText = (EditText) dialogView.findViewById(R.id.variableEditText);
        builder.setPositiveButton(R.string.button_solve_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if (game.answer(Integer.parseInt(variableEditText.getText().toString()))) {
                        if (!(pokemon.getValCur(activeButtonContext) == pokemon.getValMax(activeButtonContext))) {
                            pokemon.incVal(activeButtonContext);
                            drawStatistics();
                            Toast toast = Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Incorrect, try again!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (Exception e) {
                    System.out.println("Error!");
                }
            }
        });
        builder.setNegativeButton(R.string.button_cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }

}