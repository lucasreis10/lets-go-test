package com.catalogo.domain;

public abstract class AggregateRoot <ID extends Identifier> extends Entity{

    protected AggregateRoot(final Identifier identifier) {
        super(identifier);
    }

}
