package com.alexsmaliy.dl4s.api.query;

public interface QueryVisitor<T> {

    default T visit(VisitableBaseQuery q) {
        throw new RuntimeException("Unknown query type!");
    }

    T visit(StringQuery q);

}
