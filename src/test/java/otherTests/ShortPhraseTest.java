package otherTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhraseTest {
    @Test
    @DisplayName("Проверка длины фразы")
    public void countSymbols(String phrase) {
        int phraseLength = phrase.length();
        assertTrue(phraseLength > 15, "Amount of symbols is less than 16");

        /*в main создан объект класса, от которого вызван метод с параметром - фразой*/
    }
}
