package com.alexsmaliy.dl4s;

import com.alexsmaliy.dl4s.api.document.Field;
import com.alexsmaliy.dl4s.api.document.ImmutableTitleField;

public final class ServerConstants {
    private ServerConstants() { /* constants class */ }

    public static final String PRIMARY_INDEX_NAME = "primary-index";

    public static final class Defaults {
        private Defaults() { /* constants class */ }

        public static final boolean IGNORE_CASE = true;
        public static final Field SEARCHED_FIELD = ImmutableTitleField.of("title");
        public static final int MAX_RESULTS_TO_RETURN = 5;
    }
}
