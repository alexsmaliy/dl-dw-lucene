package solutions.bloaty.misc.dwlucene;

import solutions.bloaty.tuts.dw.deepsearch.api.document.Field;
import solutions.bloaty.tuts.dw.deepsearch.api.document.ImmutableField;

public final class Constants {
    private Constants() { /* constants class */ }

    public static final class Defaults {
        private Defaults() { /* constants class */ }

        public static final boolean IGNORE_CASE = true;
        public static final Field SEARCHED_FIELD = ImmutableField.of("title");
        public static final int MAX_RESULTS_TO_RETURN = 5;
    }
}
