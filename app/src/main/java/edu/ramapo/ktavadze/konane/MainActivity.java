/*
************************************************************
* Name:  Konstantine Tavadze                               *
* Project:  Konane                                         *
* Class:  Artificial Intelligence                          *
* Date:  03/02/2018                                        *
************************************************************
*/
package edu.ramapo.ktavadze.konane;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Game game = new Game(6, 'S', true);
    private Path hint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate board xml.
        generateBoard();
        displayTurn();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Search.
                respondToSettings();
                return true;
            case R.id.action_save:
                // Save game.
                respondToSave();
                return true;
            case R.id.action_load:
                // Load game.
                respondToLoad();
                return true;
            case R.id.action_restart:
                // Restart game.
                respondToRestart(game.boardSize, game.mode, game.guess);
                return true;
            case R.id.action_preset:
                // Load preset.
                respondToAsset("preset.txt");
                return true;
            case R.id.action_case1:
                // Load case 1.
                respondToAsset("case1.txt");
                return true;
            case R.id.action_case2:
                // Load case 2.
                respondToAsset("case2.txt");
                return true;
            default:
                // Invoke superclass to handle unrecognized action.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     Generates the xml for the board.
     */
    public void generateBoard() {
        LinearLayout board = findViewById(R.id.board);

        // Clear main view.
        board.removeAllViews();

        // Generate top column labels xml.
        generateColumnLabels();

        // Generate rows.
        for (int i = 0; i < game.boardSize; i++) {
            LinearLayout row = new LinearLayout(this);

            // Add row layout params.
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            row.setLayoutParams(rowParams);

            // Generate left row label.
            TextView rowLabel1 = new TextView(this);

            // Add row label params.
            LinearLayout.LayoutParams rowLabelParams = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            rowLabel1.setLayoutParams(rowLabelParams);

            // Add row label styles.
            rowLabel1.setBackgroundColor(Color.parseColor("#000000"));
            rowLabel1.setTextColor(Color.parseColor("#FFFFFF"));
            rowLabel1.setPadding(10, 0, 10, 0);
            rowLabel1.setGravity(Gravity.CENTER);
            rowLabel1.setTextSize(25);
            rowLabel1.setText(Integer.toString(i));

            // Add left label to row.
            row.addView(rowLabel1);

            // Generate buttons.
            for (int j = 0; j < game.boardSize; j++) {
                Button button = new Button(this);

                // Add button layout params.
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT,
                        1.0f
                );
                button.setLayoutParams(buttonParams);

                // Add button colors.
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        button.setBackgroundColor(Color.parseColor("#000000"));
                        button.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        button.setTextColor(Color.parseColor("#000000"));
                    }
                }
                else {
                    if (j % 2 == 0) {
                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        button.setTextColor(Color.parseColor("#000000"));
                    } else {
                        button.setBackgroundColor(Color.parseColor("#000000"));
                        button.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }

                // Add button styles.
                button.setTextSize(25);

                // Add button onclick listener.
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        respondToMove(view);
                    }
                });

                // Add button tag.
                String tag = "b" + i + j;
                button.setTag(tag);

                // Add button to row.
                row.addView(button);
            }

            // Generate right row label.
            TextView rowLabel2 = new TextView(this);

            // Add row label params.
            rowLabel2.setLayoutParams(rowLabelParams);

            // Add row label styles.
            rowLabel2.setBackgroundColor(Color.parseColor("#000000"));
            rowLabel2.setTextColor(Color.parseColor("#FFFFFF"));
            rowLabel2.setPadding(10, 0, 10, 0);
            rowLabel2.setGravity(Gravity.CENTER);
            rowLabel2.setTextSize(25);
            rowLabel2.setText(Integer.toString(i));

            // Add right label to row.
            row.addView(rowLabel2);

            // Add row to main.
            board.addView(row);
        }

        // Generate bottom column labels xml.
        generateColumnLabels();

        // Display the starting board.
        displayBoard();
    }

    /**
     Generates the xml for the column labels.
     */
    public void generateColumnLabels() {
        LinearLayout board = findViewById(R.id.board);

        // Generate row of column labels.
        LinearLayout colLabels = new LinearLayout(this);

        // Add column labels params.
        LinearLayout.LayoutParams colLabelsParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        colLabels.setLayoutParams(colLabelsParams);

        // Add column labels styles.
        colLabels.setBackgroundColor(Color.parseColor("#000000"));
        colLabels.setPadding(40,0,40,0);

        // Generate column labels.
        for (int c = 0; c < game.boardSize; c++) {
            TextView colLabel = new TextView(this);

            // Add column label params.
            LinearLayout.LayoutParams colLabelParams = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT,
                    1.0f
            );
            colLabel.setLayoutParams(colLabelParams);

            // Add column label styles.
            colLabel.setTextColor(Color.parseColor("#FFFFFF"));
            colLabel.setGravity(Gravity.CENTER);
            colLabel.setTextSize(25);
            colLabel.setText(Integer.toString(c));

            // Add label to column labels.
            colLabels.addView(colLabel);
        }

        // Add column labels to main.
        board.addView(colLabels);
    }

    /**
     Displays the board by setting each button's text with corresponding data.
     */
    public void displayBoard() {
        LinearLayout board = findViewById(R.id.main);
        for (int i = 0; i < game.boardSize; i++) {
            for (int j = 0; j < game.boardSize; j++) {
                String tag = "b" + i + j;
                char color = Game.board.table[i][j].color;
                if (Game.board.table[i][j].isEmpty) {
                    ((Button) board.findViewWithTag( tag )).setText(" ");
                }
                else {
                    ((Button) board.findViewWithTag( tag )).setText(Character.toString(color));
                }
            }
        }
    }

    /**
     Displays the specified message by setting the text of the first TextView.
     @param a_msg - String message to be displayed.
     */
    public void displayMessage(String a_msg) {
        ((TextView) findViewById( R.id.msg )).setText(a_msg);
    }

    /**
     Displays whose turn it is and whether she is moving by setting the text of the second TextView.
     */
    public void displayTurn() {
        String turnInfo = Character.toString(game.turn);
        if (game.turn == 'B') {
            if (game.black.isHuman) {
                turnInfo += "T";
            }
            else {
                turnInfo += "F";
            }
        }
        else {
            if (game.white.isHuman) {
                turnInfo += "T";
            }
            else {
                turnInfo += "F";
            }
        }
        ((TextView) findViewById( R.id.turn )).setText(turnInfo);
    }

    /**
     Displays the scores by setting the texts of the last two TextViews.
     */
    public void displayScores() {
        ((TextView) findViewById( R.id.black )).setText(Integer.toString(game.black.score));
        ((TextView) findViewById( R.id.white )).setText(Integer.toString(game.white.score));
    }

    /**
     Responds to the move clicks and updates the view accordingly.
     @param view - View to be listened to.
     */
    public void respondToMove(View view) {
        String tag = (String) view.getTag();

        int row = Character.getNumericValue(tag.charAt(tag.length() - 2));
        int col = Character.getNumericValue(tag.charAt(tag.length() - 1));

        // Process move.
        displayMessage(game.processMove(row, col));

        // Update user interface.
        displayBoard();
        displayTurn();
        displayScores();
        clearHint();

        // Check if game is over.
        if (game.isOver()) {
            // Display outcome.
            displayMessage(game.getResults());
        }
    }

    /**
     Responds to the pass clicks and updates the view accordingly.
     @param view - View to be listened to.
     */
    public void respondToPass(View view) {
        // Process pass.
        displayMessage(game.processPass());

        // Update user interface.
        displayTurn();
        clearHint();
    }

    /**
     Responds to the save action and updates the view accordingly.
     */
    public void respondToSave() {
        // Get game state.
        String state = game.getState();

        // Save to file.
        try {
            FileOutputStream outFile = openFileOutput("konane.txt", MODE_PRIVATE);
            OutputStreamWriter outWriter = new OutputStreamWriter(outFile);
            outWriter.write(state);
            outWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        displayMessage("Game saved!");
    }

    /**
     Responds to the load action and updates the view accordingly.
     */
    public void respondToLoad() {
        String state = "";

        // Load from file.
        try {
            FileInputStream inFile = openFileInput("konane.txt");
            InputStreamReader inReader = new InputStreamReader(inFile);
            char[] inBuffer = new char[64];
            int charRead;
            while ((charRead = inReader.read(inBuffer)) > 0) {
                String stringRead = String.copyValueOf(inBuffer, 0, charRead);
                state += stringRead;
            }
            inReader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Set game state.
        game.setState(state);

        displayMessage("Game loaded!");

        // Update user interface.
        generateBoard();
        displayBoard();
        displayTurn();
        displayScores();
        clearHint();
    }

    /**
     Responds to the asset action and updates the view accordingly.
     @param a_name - Name of the asset to be loaded.
     */
    public void respondToAsset(String a_name) {
        String state = "";

        // Load from asset.
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(a_name)));
            String lineRead;
            while ((lineRead = reader.readLine()) != null) {
                state += lineRead + "\n";
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Set game state.
        game.setState(state);

        displayMessage("Asset loaded!");

        // Update user interface.
        generateBoard();
        displayBoard();
        displayTurn();
        displayScores();
        clearHint();
    }

    /**
     Responds to the restart action and updates the view accordingly.
     @param a_size - Integer value of the board size.
     @param a_mode - Character value of the game mode.
     @param a_guess - Boolean value of the guess.
     */
    public void respondToRestart(int a_size, char a_mode, boolean a_guess) {
        // Restart game.
        game = new Game(a_size, a_mode, a_guess);

        displayMessage("Welcome!");

        // Update user interface.
        generateBoard();
        displayBoard();
        displayTurn();
        displayScores();
        clearHint();
    }

    /**
     Responds to the settings action and displays the settings dialog.
     */
    public void respondToSettings() {
        // Build settings dialog.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.settings_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.settings_title);

        // Select current size.
        final RadioGroup sizeRG = dialogView.findViewById(R.id.size);
        final RadioButton sixRB = dialogView.findViewById(R.id.six);
        final RadioButton eightRB = dialogView.findViewById(R.id.eight);
        final RadioButton tenRB = dialogView.findViewById(R.id.ten);
        switch (game.boardSize) {
            case 6:
                sixRB.setChecked(true);
                break;
            case 8:
                eightRB.setChecked(true);
                break;
            case 10:
                tenRB.setChecked(true);
                break;
        }

        // Select current mode.
        final RadioGroup modeRG = dialogView.findViewById(R.id.mode);
        final RadioButton soloRB = dialogView.findViewById(R.id.solo);
        final RadioButton multiRB = dialogView.findViewById(R.id.multi);
        switch (game.mode) {
            case 'S':
                soloRB.setChecked(true);
                break;
            case 'M':
                multiRB.setChecked(true);
                break;
        }

        // Select current guess.
        final RadioGroup guessRG = dialogView.findViewById(R.id.guess);
        final RadioButton firstRB = dialogView.findViewById(R.id.first);
        final RadioButton secondRB = dialogView.findViewById(R.id.second);
        if (game.guess) {
            firstRB.setChecked(true);
        }
        else {
            secondRB.setChecked(true);
        }

        // Define responses.
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                boolean mustRestart = false;

                // Update size.
                RadioButton sizeRB = sizeRG.findViewById(sizeRG.getCheckedRadioButtonId());
                String selectedSize = sizeRB.getText().toString();
                switch (selectedSize) {
                    case "6X6":
                        if (game.boardSize != 6) {
                            game.boardSize = 6;
                            mustRestart = true;
                        }
                        break;
                    case "8X8":
                        if (game.boardSize != 8) {
                            game.boardSize = 8;
                            mustRestart = true;
                        }
                        break;
                    case "10X10":
                        if (game.boardSize != 10) {
                            game.boardSize = 10;
                            mustRestart = true;
                        }
                        break;
                }

                // Update mode.
                RadioButton modeRB = modeRG.findViewById(modeRG.getCheckedRadioButtonId());
                String selectedMode = modeRB.getText().toString();
                switch (selectedMode) {
                    case "Solo":
                        if (game.mode != 'S') {
                            game.mode = 'S';
                            mustRestart = true;
                        }
                        break;
                    case "Multi":
                        if (game.mode != 'M') {
                            game.mode = 'M';
                            mustRestart = true;
                        }
                        break;
                }

                // Update guess
                RadioButton guessRB = guessRG.findViewById(guessRG.getCheckedRadioButtonId());
                String selectedGuess = guessRB.getText().toString();
                switch (selectedGuess) {
                    case "1st":
                        if (!game.guess) {
                            game.guess = true;
                            mustRestart = true;
                        }
                        break;
                    case "2nd":
                        if (game.guess) {
                            game.guess = false;
                            mustRestart = true;
                        }
                        break;
                }

                if (mustRestart) {
                    respondToRestart(game.boardSize, game.mode, game.guess);
                }

                displayMessage(selectedGuess + selectedSize + selectedMode);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO: Cancel.
            }
        });

        // Show settings dialog.
        AlertDialog settingsDialog = dialogBuilder.create();
        settingsDialog.show();
    }

    /**
     Responds to the next clicks and updates the view accordingly.
     @param view - View to be listened to.
     */
    public void respondToNext(View view) {
        // Clear current hint.
        clearHint();

        // Process next.
        Path next = game.processNext();

        if (next == null) return;

        // Display next hint.
        String startTag = "b" + next.visited.get(0).start.row + next.visited.get(0).start.col;
        String endTag = "b" + next.visited.get(next.visited.size() - 1).end.row + next.visited.get(next.visited.size() - 1).end.col;

        LinearLayout main = findViewById(R.id.main);
        Button startBtn = main.findViewWithTag(startTag);
        Button endBtn = main.findViewWithTag(endTag);

        startBtn.setBackgroundColor(Color.parseColor("#FFFF00"));
        endBtn.setBackgroundColor(Color.parseColor("#FFFF00"));

        String test = "";
        for (int i = 1; i < next.visited.size(); i++) {
            Move temp = next.visited.get(i);
            test += " " + temp.start.row + temp.start.col + temp.end.row + temp.end.col;
        }

        // Preview score.
        displayMessage(test);

        hint = next;
    }

    /**
     Clears the current hint.
     */
    public void clearHint() {
        if (hint != null) {
            String startTag = "b" + hint.visited.get(0).start.row + hint.visited.get(0).start.col;
            String endTag = "b" + hint.visited.get(hint.visited.size() - 1).end.row + hint.visited.get(hint.visited.size() - 1).end.col;

            LinearLayout main = findViewById(R.id.main);
            Button startBtn = main.findViewWithTag(startTag);
            Button endBtn = main.findViewWithTag(endTag);

            if (startBtn == null || endBtn == null) return;

            if (hint.visited.get(0).start.color == 'B') {
                startBtn.setBackgroundColor(Color.parseColor("#000000"));
                endBtn.setBackgroundColor(Color.parseColor("#000000"));
            }
            else if (hint.visited.get(0).start.color == 'W') {
                startBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                endBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

}
