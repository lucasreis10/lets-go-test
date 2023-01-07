package com.catalogo.domain.categoria;

import com.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoriaID extends Identifier {
    protected final String value;

    private CategoriaID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoriaID unique() {
        return CategoriaID.from(UUID.randomUUID());
    }

    public static CategoriaID from(final String id) {
        return new CategoriaID(id);
    }

    public static CategoriaID from(final UUID id) {
        return new CategoriaID(id.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaID that = (CategoriaID) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String getValue() {
        return value;
    }
}
