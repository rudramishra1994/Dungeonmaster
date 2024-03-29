package keyboard_maps;

import model.IModel;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;


/**
 * The controller is no longer implementing Swing-specific interfaces.
 */
public class Controller {
  private IModel model;
  private IView view;

  /**
   * Constructor.
   * 
   * @param m the model to use
   * @param v the view to use
   */
  public Controller(IModel m, IView v) {
    this.model = m;
    this.view = v;
    configureKeyBoardListener();
    configureButtonListener();
  }

  /**
   * Creates and sets a keyboard listener for the view. In effect it creates
   * snippets of code as Runnable object, one for each time a key is typed,
   * pressed and released, only for those that the program needs.
   *
   * <p>In this example, we need to toggle color when user TYPES 'd', make the
   * message all caps when the user PRESSES 'c' and reverts to the original string
   * when the user RELEASES 'c'. Thus we create three snippets of code
   * (ToggleColor,MakeCaps and MakeOriginalCase) and put them in the appropriate
   * map.
   *
   * <p>Last we create our KeyboardListener object, set all its maps and then give it
   * to the view.
   */
  private void configureKeyBoardListener() {
    Map<Character, Runnable> keyTypes = new HashMap<>();
    Map<Integer, Runnable> keyPresses = new HashMap<>();
    Map<Integer, Runnable> keyReleases = new HashMap<>();

    keyPresses.put(KeyEvent.VK_C, new MakeCaps());
    keyReleases.put(KeyEvent.VK_C, new MakeOriginalCase());
    // Another possible syntax: instead of defining a new class, just to make a
    // single instance,
    // you can create an "anonymous class" that implements a particular
    // interface, by writing
    // "new InterfaceName() { all the methods you need to implement }"
    // Note that "view" is in scope inside this Runnable! But, also note that
    // within the Runnable,
    // "this" refers to the Runnable and not to the Controller, so we don't say
    // "this.view".
    keyTypes.put('r', new Runnable() {
      public void run() {
        view.toggleColor();
      }
    });

    KeyboardListener kbd = new KeyboardListener();
    kbd.setKeyTypedMap(keyTypes);
    kbd.setKeyPressedMap(keyPresses);
    kbd.setKeyReleasedMap(keyReleases);

    view.addKeyListener(kbd);

  }

  /**
   * Helper that configures all of the button listeners.
   */
  private void configureButtonListener() {
    Map<String, Runnable> buttonClickedMap = new HashMap<>();
    ButtonListener buttonListener = new ButtonListener();

    buttonClickedMap.put("Echo Button", new EchoButtonAction());
    buttonClickedMap.put("Exit Button", new ExitButtonAction());

    buttonListener.setButtonClickedActionMap(buttonClickedMap);
    this.view.addActionListener(buttonListener);
  }

  // THESE CLASSES ARE NESTED INSIDE THE CONTROLLER,
  // SO THAT THEY HAVE ACCESS TO THE VIEW

  /**
   * Implementation of action to change text to all caps.
   */
  class MakeCaps implements Runnable {
    @Override
    public void run() {
      String text = model.getString();
      text = text.toUpperCase();
      view.setEchoOutput(text);
    }
  }

  /**
   * Implementation of action to change text back to original capitalization.
   */
  class MakeOriginalCase implements Runnable {
    @Override
    public void run() {
      String text = model.getString();
      view.setEchoOutput(text);
    }
  }

  /**
   * Implementation of the echo button.
   */
  class EchoButtonAction implements Runnable {
    @Override
    public void run() {
      String text = view.getInputString();
      // send text to the model
      model.setString(text);

      // clear input text field
      view.clearInputString();
      // finally echo the string in view
      text = model.getString();
      view.setEchoOutput(text);

      // set focus back to main frame so that keyboard events work
      view.resetFocus();

    }
  }

  /**
   * Implementation of the echo button.
   */
  class ExitButtonAction implements Runnable {
    @Override
    public void run() {
      System.exit(0);
    }
  }
}