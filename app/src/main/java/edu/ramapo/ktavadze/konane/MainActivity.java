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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private Game game = new Game();

    private boolean isMoving = false;
    private int r1;
    private int c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate board xml.
        generateBoard();

        // Display the starting board.
        displayBoard();
    }

    /**
     Generates the xml for the board.
     */
    public void generateBoard() {
        LinearLayout main = findViewById(R.id.main);

        // Generate rows.
        for (short i = 0; i < Board.SIZE; i++) {
            LinearLayout row = new LinearLayout(this);

            // Add row layout params.
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            row.setLayoutParams(rowParams);

            // Generate buttons.
            for (short j = 0; j < Board.SIZE; j++) {
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
                        respond(view);
                    }
                });

                // Add button tag.
                String tag = "b" + i + j;
                button.setTag(tag);

                // Add button to row.
                row.addView(button);
            }

            // Add row to main.
            main.addView(row);
        }
    }

    /**
     Displays the board by setting each button's text with corresponding data.
     */
    public void displayBoard() {
        LinearLayout board = findViewById(R.id.main);
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                String tag = "b" + i + j;
                ((Button) board.findViewWithTag( tag )).setText("" + Game.board.table[i][j].color);
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
     Responds to the button clicks and updates the view accordingly.
     @param view - View to be listened to and modified.
     */
    public void respond(View view) {
        String tag = (String) view.getTag();

        // Process the first button press.
        if (!isMoving) {
            r1 = Character.getNumericValue(tag.charAt(tag.length() - 2));
            c1 = Character.getNumericValue(tag.charAt(tag.length() - 1));

            isMoving = true;

            displayMessage("Moving " + r1 + c1 + "...");
        }
        // Process the consecutive button presses.
        else {
            int r2 = Character.getNumericValue(tag.charAt(tag.length() - 2));
            int c2 = Character.getNumericValue(tag.charAt(tag.length() - 1));

            // Process the move.
            String move = game.processMove(r1, c1, r2, c2);
            if (move.equals("Illegal")) {
                isMoving = false;

                displayMessage("Illegal move!");
            }
            else if (move.equals("Basic")) {
                isMoving = false;

                displayMessage("...to " + r2 + c2);
            }
            else if (move.equals("Combo")) {
                r1 = r2;
                c1 = c2;

                displayMessage("...to " + r2 + c2);
            }
        }

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

}
