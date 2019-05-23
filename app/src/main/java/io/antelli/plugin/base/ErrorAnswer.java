package io.antelli.plugin.base;

import io.antelli.sdk.model.Answer;
import io.antelli.sdk.model.AnswerItem;

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

public class ErrorAnswer extends Answer {

    public ErrorAnswer() {
        addItem(new AnswerItem()
        .setText("Něco se pokazilo, zkontroluj připojení a zkus to za chvíli.")
        .setSpeech("Něco se pokazilo, zkontroluj připojení a zkus to za chvíli."));
    }
}
