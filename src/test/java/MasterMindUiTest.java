import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;

class MasterMindUITest {

    static class TestLogic extends MasterMindLogic {
        public TestLogic(Color[] palette, String[] labels) {
            super(palette, 4, labels);
        }

        @Override
        public Result checkGuess(Color[] guess) {
            return new Result(2, 2);
        }

        @Override
        public String showSecret() {
            return "TEST";
        }
    }

    @Test
    void testUIInteractionAndPainting() {
        Color[] palette = {Color.RED, Color.BLUE};
        String[] labels = {"R", "B"};

        TestLogic logic = new TestLogic(palette, labels);

        MasterMindUI ui = new MasterMindUI(palette, labels, 2, logic); // 2 rondas
        assertNotNull(ui.guessRows);

        MasterMindUI.Circle testCircle = new MasterMindUI.Circle(Color.RED, 10, true);
        assertEquals(Color.RED, testCircle.getColor());

        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        testCircle.paint(g);
        g.dispose();

        JPanel bottomPanel = (JPanel) ((java.awt.BorderLayout)ui.createBottomPanel(palette, labels).getLayout()).getLayoutComponent("West");

        ui.selectedColor = Color.BLUE;

        MasterMindUI.Circle slot = ui.guessRows.get(0)[0];
        slot.getActionListeners()[0].actionPerformed(null); // Simula el clic
        assertEquals(Color.BLUE, slot.getColor(), "El círculo debería cambiar a Azul");

        for (MasterMindUI.Circle c : ui.guessRows.get(0)) {
            ui.selectedColor = Color.RED;
            c.setCircleColor(Color.RED);
        }

        MasterMindLogic.Result mockResult = logic.checkGuess(new Color[]{Color.RED, Color.RED, Color.RED, Color.RED});
        ui.colorPins(ui.pinRows.get(0), mockResult);
        ui.currentRow++;

        MasterMindUI.Circle[] feedbackPins = ui.pinRows.get(0);
        assertEquals(Color.BLACK, feedbackPins[0].getColor());
        assertEquals(Color.BLACK, feedbackPins[1].getColor());
        assertEquals(Color.WHITE, feedbackPins[2].getColor());
        assertEquals(Color.WHITE, feedbackPins[3].getColor());

        ui.currentRow = 9;
    }
    @Test
    void testCoverageBooster() {
        Color[] palette = {Color.RED, Color.BLUE};
        String[] labels = {"R", "B"};

        MasterMindLogic logic = new MasterMindLogic(palette, 4, labels);
        MasterMindUI ui = new MasterMindUI(palette, labels, 2, logic);

        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        MasterMindUI.Circle pin = ui.pinRows.get(0)[0];
        pin.paint(g);

        MasterMindUI.Circle slot = ui.guessRows.get(0)[0];
        slot.paint(g);
        assertNotNull(pin.getPreferredSize());
        assertNotNull(slot.getPreferredSize());
        g.dispose();

        JPanel bottomPanel = (JPanel) ui.createBottomPanel(palette, labels);

        JPanel colorStrip = (JPanel) ((java.awt.BorderLayout)bottomPanel.getLayout()).getLayoutComponent("West");

        for (java.awt.Component comp : colorStrip.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;

                btn.getActionListeners()[0].actionPerformed(null);
            }
        }
        assertNotNull(ui.selectedColor);


        MasterMindUI.Circle wrongRowCircle = ui.guessRows.get(1)[0];
        Color originalColor = wrongRowCircle.getColor();

        wrongRowCircle.getActionListeners()[0].actionPerformed(null);

        assertEquals(originalColor, wrongRowCircle.getColor());


        MasterMindUI.Circle correctRowCircle = ui.guessRows.get(0)[0];
        ui.selectedColor = Color.YELLOW;

        correctRowCircle.getActionListeners()[0].actionPerformed(null);

        assertEquals(Color.YELLOW, correctRowCircle.getColor());
    }
}