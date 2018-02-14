/*
************************************************************
* Name:  Konstantine Tavadze                               *
* Project:  Project 1 - Konane                             *
* Class:  CMPS331 - Artificial Intelligence                *
* Date:  02/02/2018                                        *
************************************************************
*/
package edu.ramapo.ktavadze.konane;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate board xml.
        generateBoard();
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
     @param view - View to be listened to and modified.
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

        // Check if game is over.
        if (game.isOver()) {
            // Display outcome.
            displayMessage(game.getResults());
        }
    }

    /**
     Responds to the pass clicks and updates the view accordingly.
     @param view - View to be listened to and modified.
     */
    public void respondToPass(View view) {
        // Process move.
        displayMessage(game.processPass());

        // Update user interface.
        displayTurn();
    }

    /**
     Responds to the save clicks and updates the view accordingly.
     @param view - View to be listened to and modified.
     */
    public void respondToSave(View view) {
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
     Responds to the load clicks and updates the view accordingly.
     @param view - View to be listened to and modified.
     */
    public void respondToLoad(View view) {
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
    }

}
