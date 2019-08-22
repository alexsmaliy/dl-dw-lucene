package solutions.bloaty.tuts.dw.deepsearch.api;

import solutions.bloaty.tuts.dw.deepsearch.api.document.Field;
import solutions.bloaty.tuts.dw.deepsearch.api.document.ImmutableTitleField;

public class ApiConstants {
    private ApiConstants() { /* constants class */ }

    public static final class Defaults {
        private Defaults() { /* constants class */ }

        public static final boolean IGNORE_CASE = true;
        public static final Field SEARCHED_FIELD = ImmutableTitleField.of("title");
        public static final int MAX_RESULTS_TO_RETURN = 5;
    }
}
