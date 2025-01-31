/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.index.mapper;

import org.elasticsearch.common.Strings;

/**
 * Holds context for building Mapper objects from their Builders
 */
public class MapperBuilderContext {

    private static final MapperBuilderContext ROOT_SYNTHETIC = new MapperBuilderContext(null, true);
    private static final MapperBuilderContext ROOT_NOT_SYNTHETIC = new MapperBuilderContext(null, false);

    /**
     * The root context, to be used when building a tree of mappers
     */
    public static MapperBuilderContext root(boolean isSourceSynthetic) {
        return isSourceSynthetic ? ROOT_SYNTHETIC : ROOT_NOT_SYNTHETIC;
    }

    private final String path;
    private final boolean isSourceSynthetic;

    MapperBuilderContext(String path, boolean isSourceSynthetic) {
        this.path = path;
        this.isSourceSynthetic = isSourceSynthetic;
    }

    /**
     * Creates a new MapperBuilderContext that is a child of this context
     * @param name the name of the child context
     * @return a new MapperBuilderContext with this context as its parent
     */
    public MapperBuilderContext createChildContext(String name) {
        return new MapperBuilderContext(buildFullName(name), isSourceSynthetic);
    }

    /**
     * Builds the full name of the field, taking into account parent objects
     */
    public String buildFullName(String name) {
        if (Strings.isEmpty(path)) {
            return name;
        }
        return path + "." + name;
    }

    /**
     * Is the {@code _source} field being reconstructed on the fly?
     */
    public boolean isSourceSynthetic() {
        return isSourceSynthetic;
    }

}
