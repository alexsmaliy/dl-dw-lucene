package com.alexsmaliy.dl4s.api;

import com.alexsmaliy.dl4s.api.document.ImmutableTitleField;
import com.alexsmaliy.dl4s.api.document.Field;

public class ApiConstants {
    private ApiConstants() { /* constants class */ }

    public static final class Defaults {
        private Defaults() { /* constants class */ }

        public static final boolean IGNORE_CASE = true;
        public static final Field SEARCHED_FIELD = ImmutableTitleField.of("title");
        public static final int MAX_RESULTS_TO_RETURN = 5;
    }
}
