package solutions.bloaty.misc.dwlucene;

import solutions.bloaty.tuts.dw.deepsearch.api.document.Field;
import solutions.bloaty.tuts.dw.deepsearch.api.document.ImmutableField;

public final class Constants {
    private Constants() { /* constants class */ }

    public static final boolean DEFAULT_IGNORE_CASE = true;
    public static final Field DEFAULT_SEARCHED_FIELD = ImmutableField.of("title");
}
