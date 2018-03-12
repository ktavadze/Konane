/*
************************************************************
* Name:  Konstantine Tavadze                               *
* Project:  Konane                                         *
* Class:  CMPS331 - Artificial Intelligence                *
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Game game = new Game();
    private String hint = "";
    private String mode = "Solo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate board xml.
        generateBoard();
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
                respondToRestart();
                return true;
            case R.id.action_preset:
                // Load preset.
                game.setState("Black: 6\n" +
                        "White: 4\n" +
                        "Board:\n" +
                        "B W B W B W\n" +
                        "W B O B O B\n" +
                        "B W O W B W\n" +
                        "O B O O O O\n" +
                        "B W B O O W\n" +
                        "W B W O O B\n" +
                        "Next Player: White");

                displayMessage("Preset loaded!");

                // Update user interface.
                displayBoard();
                displayTurn();
                displayScores();
                clearHint();
                return true;
            case R.id.action_case1:
                // Load case 1.
                game.setState("Black: 0\n" +
                        "White: 0\n" +
                        "Board:\n" +
                        "O W O W O O\n" +
                        "W O W O W O\n" +
                        "O W B W O O\n" +
                        "W O W O W O\n" +
                        "O W O W O O\n" +
                        "O O O O O O\n" +
                        "Next Player: Black");

                displayMessage("Case 1 loaded!");

                // Update user interface.
                displayBoard();
                displayTurn();
                displayScores();
                clearHint();
                return true;
            case R.id.action_case2:
                // Load case 2.
                game.setState("Black: 0\n" +
                        "White: 0\n" +
                        "Board:\n" +
                        "O W O O O O\n" +
                        "O O W O W O\n" +
                        "O W B W O O\n" +
                        "W O W O O O\n" +
                        "O W O O O W\n" +
                        "W O W O W O\n" +
                        "Next Player: Black");

                displayMessage("Case 2 loaded!");

                // Update user interface.
                displayBoard();
                displayTurn();
                displayScores();
                clearHint();
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
        LinearLayout main = findViewById(R.id.main);

        // Generate top column labels xml.
        generateColumnLabels();

        // Generate rows.
        for (int i = 0; i < Board.SIZE; i++) {
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
            rowLabel1.setText("" + (i + 1));

            // Add left label to row.
            row.addView(rowLabel1);

            // Generate buttons.
            for (int j = 0; j < Board.SIZE; j++) {
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
            rowLabel2.setText("" + (i + 1));

            // Add right label to row.
            row.addView(rowLabel2);

            // Add row to main.
            main.addView(row);
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
        LinearLayout main = findViewById(R.id.main);

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
        for (int c = 1; c <= Board.SIZE; c++) {
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
            colLabel.setText("" + c);

            // Add label to column labels.
            colLabels.addView(colLabel);
        }

        // Add column labels to main.
        main.addView(colLabels);
    }

    /**
     Displays the board by setting each button's text with corresponding data.
     */
    public void displayBoard() {
        LinearLayout board = findViewById(R.id.main);
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                String tag = "b" + i + j;
                char color = Game.board.table[i][j].color;
                if (color == 'O') {
                    ((Button) board.findViewWithTag( tag )).setText(" ");
                }
                else {
                    ((Button) board.findViewWithTag( tag )).setText("" + color);
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
        ((TextView) findViewById( R.id.turn )).setText("" + game.turn);
    }

    /**
     Displays the scores by setting the texts of the last two TextViews.
     */
    public void displayScores() {
        ((TextView) findViewById( R.id.black )).setText("" + game.black.score);
        ((TextView) findViewById( R.id.white )).setText("" + game.white.score);
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
        displayBoard();
        displayTurn();
        displayScores();
        clearHint();
    }

    /**
     Responds to the restart action and updates the view accordingly.
     */
    public void respondToRestart() {
        // Restart game.
        game = new Game();

        displayMessage("Welcome!");

        // Update user interface.
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
        final RadioGroup sizeRG = (RadioGroup) dialogView.findViewById(R.id.size);
        final RadioButton sixRB = (RadioButton) dialogView.findViewById(R.id.six);
        final RadioButton eightRB = (RadioButton) dialogView.findViewById(R.id.eight);
        final RadioButton tenRB = (RadioButton) dialogView.findViewById(R.id.ten);
        switch (game.board.SIZE) {
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
        final RadioGroup modeRG = (RadioGroup) dialogView.findViewById(R.id.mode);
        final RadioButton soloRB = (RadioButton) dialogView.findViewById(R.id.solo);
        final RadioButton multiRB = (RadioButton) dialogView.findViewById(R.id.multi);
        switch (mode) {
            case "Solo":
                soloRB.setChecked(true);
                break;
            case "Multi":
                multiRB.setChecked(true);
                break;
        }

        // Select current search.
        final Spinner searchSPN = (Spinner) dialogView.findViewById(R.id.search);
        switch (game.search) {
            case "Depth":
                searchSPN.setSelection(0);
                break;
            case "Breadth":
                searchSPN.setSelection(1);
                break;
            case "Best":
                searchSPN.setSelection(2);
                break;
        }

        // Define responses.
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Update guess
                EditText guessET = dialogView.findViewById(R.id.guess);
                String selectedGuess = guessET.getText().toString();

                // Update size.
                RadioButton sizeRB = sizeRG.findViewById(sizeRG.getCheckedRadioButtonId());
                String selectedSize = sizeRB.getText().toString();

                // Update mode.
                RadioButton modeRB = modeRG.findViewById(modeRG.getCheckedRadioButtonId());
                String selectedMode = modeRB.getText().toString();

                // Update search.
                String selectedSearch = searchSPN.getSelectedItem().toString();
                game.setSearch(selectedSearch);

                displayMessage(selectedGuess + selectedSize + selectedMode + selectedSearch);
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
        // Process next.
        String next = game.processNext();

        // Clear current hint.
        clearHint();

        // Display next hint.
        String startTag = "b" + next.charAt(1) + next.charAt(2);
        String endTag = "b" + next.charAt(3) + next.charAt(4);

        LinearLayout main = findViewById(R.id.main);
        Button startBtn = main.findViewWithTag(startTag);
        Button endBtn = main.findViewWithTag(endTag);

        startBtn.setBackgroundColor(Color.parseColor("#FFFF00"));
        endBtn.setBackgroundColor(Color.parseColor("#FFFF00"));

        // Preview score.
        displayMessage("Score: +" + next.charAt(5));

        hint = next;
    }

    /**
     Clears the current hint.
     */
    public void clearHint() {
        if (!hint.equals("")) {
            String startTag = "b" + hint.charAt(1) + hint.charAt(2);
            String endTag = "b" + hint.charAt(3) + hint.charAt(4);

            LinearLayout main = findViewById(R.id.main);
            Button startBtn = main.findViewWithTag(startTag);
            Button endBtn = main.findViewWithTag(endTag);

            if (hint.charAt(0) == 'B') {
                startBtn.setBackgroundColor(Color.parseColor("#000000"));
                endBtn.setBackgroundColor(Color.parseColor("#000000"));
            }
            else {
                startBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                endBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

}
