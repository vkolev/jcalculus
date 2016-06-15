package net.vkolev;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vlado on 15.06.16.
 */
public class CalcController {

    private static final Logger log = LoggerFactory.getLogger(CalcController.class);

    @FXML
    private TextField mainField;
    @FXML
    private Button clrButton;
    @FXML
    private Button bckButton;

    public void clearField() {
        mainField.clear();
    }

    public void bckField() {
        if(!mainField.getText().equals("")) {
            mainField.setText(mainField.getText().substring(0, mainField.getText().length() - 1));
        }
    }

    public void addOpenBracket() {
        mainField.insertText(mainField.getLength(), "(");
    }

    public void addCloseBracket() {
        mainField.insertText(mainField.getLength(), ")");
    }

    public void insertSeven() {
        mainField.insertText(mainField.getLength(), "7");
    }

    public void insertEight() {
        mainField.insertText(mainField.getLength(), "8");
    }

    public void insertNine() {
        mainField.insertText(mainField.getLength(), "9");
    }

    public void insertFour() {
        mainField.insertText(mainField.getLength(), "4");
    }

    public void insertFive() {
        mainField.insertText(mainField.getLength(), "5");
    }

    public void insertSix() {
        mainField.insertText(mainField.getLength(), "6");
    }

    public void insertOne() {
        mainField.insertText(mainField.getLength(), "1");
    }

    public void insertTwo() {
        mainField.insertText(mainField.getLength(), "2");
    }

    public void insertThree() {
        mainField.insertText(mainField.getLength(), "3");
    }

    public void insertZero() {
        mainField.insertText(mainField.getLength(), "0");
    }

    public void insertDevide() {
        mainField.insertText(mainField.getLength(), "/");
    }

    public void insertMultiply() {
        mainField.insertText(mainField.getLength(), "*");
    }

    public void insertSub() {
        mainField.insertText(mainField.getLength(), "-");
    }

    public void insertAdd() {
        mainField.insertText(mainField.getLength(), "+");
    }

    public void insertPoint() {
        mainField.insertText(mainField.getLength(), ".");
    }

	public void insertSqrt() {
		mainField.insertText(mainField.getLength(), "sqrt");
	}

	public void insertSin() {
		// TODO
	}

	public void insertCos() {
		// TODO
	}

	public void insertTan() {
		// TODO
	}

    public void calculateExpr() {
        double result = eval(mainField.getText());
        mainField.setText(fmt(result));
    }

    public static String fmt(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public void handleKeyPressedEvent(KeyEvent keyEvent) {

    }

    public void handleKeyReleasedEvent(KeyEvent keyEvent) {
        //System.out.println(keyEvent.getCode());
        if(keyEvent.getCode() == KeyCode.ADD) {
            this.insertAdd();
        }
        if(keyEvent.getCode() == KeyCode.MINUS || keyEvent.getCode() == KeyCode.SUBTRACT) {
            this.insertSub();
        }
        if(keyEvent.getCode() == KeyCode.MULTIPLY) {
            this.insertMultiply();
        }
        if(keyEvent.getCode() == KeyCode.DIVIDE) {
            this.insertDevide();
        }
        if(keyEvent.getCode() == KeyCode.BACK_SPACE) {
            this.bckField();
        }
        if(keyEvent.getCode() == KeyCode.ESCAPE) {
            this.clearField();
        }
        if(keyEvent.getCode() == KeyCode.ENTER) {
            this.calculateExpr();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT0 || keyEvent.getCode() == KeyCode.NUMPAD0) {
            this.insertZero();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT1 || keyEvent.getCode() == KeyCode.NUMPAD1) {
            this.insertOne();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT2 || keyEvent.getCode() == KeyCode.NUMPAD2) {
            this.insertTwo();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT3 || keyEvent.getCode() == KeyCode.NUMPAD3) {
            this.insertThree();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT4 || keyEvent.getCode() == KeyCode.NUMPAD4) {
            this.insertFour();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT5 || keyEvent.getCode() == KeyCode.NUMPAD5) {
            this.insertFive();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT6 || keyEvent.getCode() == KeyCode.NUMPAD6) {
            this.insertSix();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT7 || keyEvent.getCode() == KeyCode.NUMPAD7) {
            this.insertSeven();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT8 || keyEvent.getCode() == KeyCode.NUMPAD8) {
            this.insertEight();
        }
        if(keyEvent.getCode() == KeyCode.DIGIT9 || keyEvent.getCode() == KeyCode.NUMPAD9) {
            this.insertNine();
        }

        final KeyCombination keyCombQuitGNOME = new KeyCodeCombination(KeyCode.W,
                KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombQuitOTHER = new KeyCodeCombination(KeyCode.Q,
                KeyCombination.ALT_DOWN);
		final KeyCombination keyCombCopy = new KeyCodeCombination(KeyCode.C,
				KeyCombination.CONTROL_DOWN);
        if(keyCombQuitGNOME.match(keyEvent) || keyCombQuitOTHER.match(keyEvent)) {
            Stage stage = (Stage) clrButton.getScene().getWindow();
            stage.close();
        }

		if(keyCombCopy.match(keyEvent)) {
			final Clipboard clipboard = Clipboard.getSystemClipboard();
			final ClipboardContent content = new ClipboardContent();
			content.putString(mainField.getText());
			clipboard.setContent(content);
		}
    }
}
