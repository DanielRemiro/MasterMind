import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.Color;

class MasterMindLogicTest {

    private final Color[] palette = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    private final String[] labels = {"R", "B", "G", "Y"};

    @Test
    void testFullCoverageFlow() {
        MasterMindLogic logic = new MasterMindLogic(palette, 4, labels);
        assertNotNull(logic.SECRET);
        assertEquals(4, logic.SECRET.length);

        MasterMindLogic.Result winResult = logic.checkGuess(logic.SECRET);
        assertEquals(4, winResult.blacks);
        assertEquals(0, winResult.whites);

        Color fakeColor = new Color(1, 2, 3);
        Color[] wrongGuess = {fakeColor, fakeColor, fakeColor, fakeColor};
        MasterMindLogic.Result failResult = logic.checkGuess(wrongGuess);
        assertEquals(0, failResult.blacks);
        assertEquals(0, failResult.whites);

        Color[] whiteGuess = new Color[4];
        whiteGuess[0] = logic.SECRET[3];
        whiteGuess[1] = logic.SECRET[2];
        whiteGuess[2] = logic.SECRET[1];
        whiteGuess[3] = logic.SECRET[0];

        logic.checkGuess(whiteGuess);

        String secretString = logic.showSecret();
        assertNotNull(secretString);
        assertEquals(4, secretString.length());

        boolean foundLabel = false;
        for (String label : labels) {
            if (secretString.contains(label)) foundLabel = true;
        }
        assertTrue(foundLabel);
    }

    @Test
    void testResultClass() {
        MasterMindLogic.Result r = new MasterMindLogic.Result(1, 2);
        assertEquals(1, r.blacks);
        assertEquals(2, r.whites);
    }
}